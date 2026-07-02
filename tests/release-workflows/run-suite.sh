#!/bin/bash
# Executes the ACTUAL `run:` blocks of release-qa.yml and release-promote.yml
# against an isolated local clone of this repo, covering the release state
# machine end to end: initial RC cut, rc.2 update, resume after every partial
# failure, promote (fresh + both resume states), and every guard — including
# negative cases seeded with the real content that broke the 3.7.0 release.
#
# Runs anywhere with GNU coreutils, git, and python3+PyYAML (GitHub runners
# and Linux both qualify; on macOS run it inside a Linux container).
# Everything happens in a throwaway temp dir: no network, no pushes anywhere
# except a local bare repo created for the run.
#
# Usage: bash tests/release-workflows/run-suite.sh [repo-root]
set -uo pipefail

REPO_ROOT="${1:-$(git rev-parse --show-toplevel)}"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT
STEPS="$WORK/steps"
ORIGIN="$WORK/origin.git"
export GITHUB_OUTPUT="$WORK/gh_output"
export GIT_CONFIG_GLOBAL="$WORK/gitconfig"
git config --global user.name suite && git config --global user.email suite@test
git config --global init.defaultBranch main
git config --global --add safe.directory '*'   # containers: repo may be owned by another uid

PASS=0; FAIL=0
ok()  { echo "PASS: $1"; PASS=$((PASS+1)); }
bad() { echo "FAIL: $1"; FAIL=$((FAIL+1)); }
outv() { grep "^$1=" "$GITHUB_OUTPUT" | tail -1 | cut -d= -f2-; }

# ---- extract the real run blocks from the workflow YAML ----
mkdir -p "$STEPS"
python3 - "$REPO_ROOT" "$STEPS" <<'PYEOF'
import re, sys, yaml
repo, steps_dir = sys.argv[1], sys.argv[2]
for wf in ("release-qa", "release-promote"):
    with open(f"{repo}/.github/workflows/{wf}.yml") as f:
        doc = yaml.safe_load(f)
    for job in doc["jobs"].values():
        for i, step in enumerate(job["steps"]):
            if "run" not in step:
                continue
            name = re.sub(r"[^A-Za-z0-9]+", "-", step.get("name", f"step{i}")).lower()
            with open(f"{steps_dir}/{wf}--{name}.sh", "w") as f:
                f.write(step["run"])
print("extracted step scripts")
PYEOF
[ -f "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" ] || { echo "extraction failed"; exit 1; }

# ---- build the isolated origin (all history + tags, plus a main branch) ----
git clone -q --bare "$REPO_ROOT" "$ORIGIN"
if ! git -C "$ORIGIN" rev-parse -q --verify refs/heads/main >/dev/null; then
  # CI checkouts are detached: materialize main from the source's origin/main.
  git -C "$ORIGIN" update-ref refs/heads/main "$(git -C "$REPO_ROOT" rev-parse origin/main)"
fi
if git -C "$ORIGIN" ls-remote . 'refs/heads/releases/9*' | grep -q .; then
  echo "PRECONDITION FAIL: origin already has suite branches"; exit 99
fi

# ---- derive test versions that can never collide with real releases ----
clone() { rm -rf "$WORK/$1" && git clone -q "$ORIGIN" "$WORK/$1" && cd "$WORK/$1"; }
clone probe
DEV_VERSION=$(python3 -c "
import xml.etree.ElementTree as ET
ns={'m':'http://maven.apache.org/POM/4.0.0'}
print(ET.parse('pom.xml').getroot().find('m:version',ns).text)")
DEV_BASE="${DEV_VERSION%-SNAPSHOT}"
MAJ="${DEV_BASE%%.*}"; MIN=$(echo "$DEV_BASE" | cut -d. -f2)
# Patch versions one minor behind the dev version: below dev (so the no-bump
# semantics of a patch release hold) and with .9x patches no real release uses.
TV="$MAJ.$((MIN-1)).99"    # main initial-cut + resume + promote scenarios
TV2="$MAJ.$((MIN-1)).98"   # hand-finalized-refusal scenario
TV3="$MAJ.$((MIN-1)).97"   # base_ref-not-found scenario
RELEASED=$(git tag -l | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' | sort -V | tail -1)
for t in "$TV" "$TV2" "$TV3"; do
  git rev-parse -q --verify "refs/tags/$t" >/dev/null && { echo "PRECONDITION FAIL: tag $t exists"; exit 99; }
done
echo "dev=$DEV_VERSION  test versions: $TV / $TV2 / $TV3  released sample: $RELEASED"

echo "=== I: qa initial cut ==="
clone w1
export V="$TV" CORE=10.0.0 BASE_REF=main
: > "$GITHUB_OUTPUT"
bash "$STEPS/release-qa--validate-inputs.sh" && ok "I validate-inputs" || bad "I validate-inputs"
bash "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" || bad "I setup errored"
[ "$(outv mode)" = "initial" ] && [ "$(outv rc_tag)" = "$TV-rc.1" ] && [ "$(outv old_version)" = "$DEV_VERSION" ] \
  && ok "I setup outputs correct" || bad "I setup outputs: $(cat "$GITHUB_OUTPUT")"
bash "$STEPS/release-qa--pin-frontend-versions.sh" && ok "I pin ran" || bad "I pin errored"
export OLD="$(outv old_version)" NEW="$(outv rc_tag)"
bash "$STEPS/release-qa--set-project-versions.sh" >/dev/null && ok "I set-versions ran" || bad "I set-versions errored"
bash "$STEPS/release-qa--refuse-snapshot-or-timestamp-locked-module-versions.sh" && ok "I snapshot guard clean" || bad "I snapshot guard tripped"
export RC_TAG="$(outv rc_tag)"
bash "$STEPS/release-qa--create-release-commit-and-tag.sh" >/dev/null && ok "I commit+tag ran" || bad "I commit+tag errored"
n=$(git show --stat --format= HEAD | grep -c '|')
[ "$n" = "5" ] && ok "I commit touches 5 files" || bad "I commit touches $n files"
grep -q "<version>$TV-rc.1</version>" pom.xml && ok "I pom content actually at rc.1" || bad "I pom content wrong: $(grep -m1 '<version>' pom.xml)"
bash "$STEPS/release-qa--push-release-branch-and-tag.sh" >/dev/null 2>&1 && ok "I push step ran (to local origin)" || bad "I push errored"

echo "=== II: qa resume after tag-push failure ==="
clone w2
git tag -d "$TV-rc.1" >/dev/null 2>&1; git push -q origin ":refs/tags/$TV-rc.1" 2>/dev/null
: > "$GITHUB_OUTPUT"
export V="$TV" CORE="" BASE_REF=main
bash "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" || bad "II setup errored"
[ "$(outv mode)" = "update" ] && [ "$(outv rc_tag)" = "$TV-rc.1" ] && ok "II re-derives rc.1 in update mode" || bad "II outputs: $(cat "$GITHUB_OUTPUT")"
C1=$(git rev-parse HEAD)
bash "$STEPS/release-qa--pin-frontend-versions.sh" | grep -q "Keeping app shell" && ok "II empty core keeps pinned shell" || bad "II pin behavior"
export OLD="$(outv old_version)" NEW="$(outv rc_tag)" RC_TAG="$(outv rc_tag)"
bash "$STEPS/release-qa--set-project-versions.sh" >/dev/null && ok "II set-versions no-op ok" || bad "II set-versions errored"
out=$(bash "$STEPS/release-qa--create-release-commit-and-tag.sh")
echo "$out" | grep -q "resuming a previous partial run" && ok "II resume detected, no empty-commit crash" || bad "II resume: $out"
[ "$(git rev-parse HEAD)" = "$C1" ] && [ "$(git rev-parse "$TV-rc.1^{commit}")" = "$C1" ] && ok "II tag at original commit, no extra commit" || bad "II tag/commit wrong"
git push -q origin "refs/tags/$TV-rc.1"

echo "=== III: promote fresh path (docker pin simulated) ==="
clone w3
: > "$GITHUB_OUTPUT"
export V="$TV"
bash "$STEPS/release-promote--validate-inputs.sh" && ok "III validate" || bad "III validate"
bash "$STEPS/release-promote--check-out-release-branch-and-locate-the-qa-approved-rc.sh" || bad "III setup errored"
[ "$(outv rc_tag)" = "$TV-rc.1" ] && [ "$(outv core)" = "10.0.0" ] && [ "$(outv resume)" = "false" ] \
  && ok "III setup: rc located, resume=false" || bad "III outputs: $(cat "$GITHUB_OUTPUT")"
# stand-in for the docker-based manifest pin step (needs Docker Hub):
sed -i 's/"latest"/"1.0.0"/g' frontend/spa-assemble-config.json
export OLD="$(outv rc_tag)" NEW="$V"
bash "$STEPS/release-promote--set-project-versions.sh" && ok "III set final versions" || bad "III set-versions"
bash "$STEPS/release-promote--refuse-snapshot-or-timestamp-locked-module-versions.sh" && ok "III snapshot guard clean" || bad "III guard tripped"
export RESUME="$(outv resume)"
bash "$STEPS/release-promote--create-release-commit-and-tag.sh" >/dev/null && ok "III final commit+tag" || bad "III commit errored"
git push -q origin "releases/$V"   # simulate: branch pushed, tag push failed

echo "=== IV: promote resume (final commit pushed, tag lost) ==="
clone w4
: > "$GITHUB_OUTPUT"
export V="$TV"
out=$(bash "$STEPS/release-promote--check-out-release-branch-and-locate-the-qa-approved-rc.sh")
echo "$out" | grep -q "Resuming a previous partial promote" && ok "IV resume detected" || bad "IV resume not detected"
[ "$(outv resume)" = "true" ] && ok "IV resume=true output" || bad "IV outputs: $(cat "$GITHUB_OUTPUT")"
C_FINAL=$(git rev-parse HEAD)
export RESUME=true
out=$(bash "$STEPS/release-promote--create-release-commit-and-tag.sh")
echo "$out" | grep -q "Using the final commit already on the branch" && ok "IV no duplicate final commit" || bad "IV commit step: $out"
[ "$(git rev-parse "$V^{commit}")" = "$C_FINAL" ] && [ "$(git rev-parse HEAD)" = "$C_FINAL" ] && ok "IV tag at existing final commit" || bad "IV tag wrong"

echo "=== V: promote refuses foreign commits past the rc ==="
git tag -d "$V" >/dev/null
echo junk >> README.md && git add -A && git commit -qm "someone's commit"
: > "$GITHUB_OUTPUT"
out=$(bash "$STEPS/release-promote--check-out-release-branch-and-locate-the-qa-approved-rc.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "commits after" && ok "V moved-past-rc refused" || bad "V: rc=$rc $out"

echo "=== VI: promote resume after tag pushed but dispatch failed ==="
git reset -q --hard HEAD~1
git tag "$V" && git push -q origin "refs/tags/$V"
clone w5
: > "$GITHUB_OUTPUT"
export V="$TV"
out=$(bash "$STEPS/release-promote--check-out-release-branch-and-locate-the-qa-approved-rc.sh")
echo "$out" | grep -q "resuming to re-dispatch" && ok "VI tag-exists resume detected" || bad "VI: $out"
[ "$(outv resume)" = "true" ] && ok "VI resume=true" || bad "VI outputs: $(cat "$GITHUB_OUTPUT")"
C_FINAL=$(git rev-parse HEAD)
export RESUME=true
bash "$STEPS/release-promote--create-release-commit-and-tag.sh" >/dev/null && ok "VI commit step tolerates existing tag" || bad "VI commit step errored"
[ "$(git rev-parse HEAD)" = "$C_FINAL" ] && [ "$(git rev-parse "$V^{commit}")" = "$C_FINAL" ] && ok "VI no new commit, tag unchanged" || bad "VI state changed"
bash "$STEPS/release-promote--push-release-branch-and-tag.sh" >/dev/null 2>&1 && ok "VI push idempotent with existing remote tag" || bad "VI push errored"

echo "=== VII: promote refuses a hand-finalized commit with unpinned frontend ==="
clone w6
export V="$TV2" CORE=10.0.0 BASE_REF=main
: > "$GITHUB_OUTPUT"
bash "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" >/dev/null || bad "VII qa setup errored"
bash "$STEPS/release-qa--pin-frontend-versions.sh" >/dev/null || bad "VII pin errored"
export OLD="$(outv old_version)" NEW="$(outv rc_tag)" RC_TAG="$(outv rc_tag)"
bash "$STEPS/release-qa--set-project-versions.sh" >/dev/null && bash "$STEPS/release-qa--create-release-commit-and-tag.sh" >/dev/null || bad "VII rc cut errored"
git push -q origin "releases/$TV2" "refs/tags/$TV2-rc.1"
for f in pom.xml distro/pom.xml frontend/pom.xml; do sed -i "s|<version>$TV2-rc.1</version>|<version>$TV2</version>|" "$f"; done
git add -A && git commit -qm "manually finalize $TV2" && git push -q origin "releases/$TV2"
clone w7
: > "$GITHUB_OUTPUT"
export V="$TV2"
out=$(bash "$STEPS/release-promote--check-out-release-branch-and-locate-the-qa-approved-rc.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "not exact-pinned" && ok "VII refused unpinned hand-finalized commit" || bad "VII: rc=$rc $out"

echo "=== VIII: qa refuses an already-released version ==="
clone w8
export V="$RELEASED" CORE=10.0.0 BASE_REF=main
out=$(bash "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "already released" && ok "VIII refused re-releasing $RELEASED" || bad "VIII: rc=$rc $out"

echo "=== IX: qa fails clearly on a nonexistent base_ref ==="
export V="$TV3" CORE=10.0.0 BASE_REF=no-such-ref
out=$(bash "$STEPS/release-qa--determine-release-mode-and-rc-number.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "base_ref 'no-such-ref' not found" && ok "IX base_ref not-found error" || bad "IX: rc=$rc $out"

echo "=== X: qa pin refuses empty core_version on an unpinned tree ==="
git checkout -q main 2>/dev/null || git checkout -q -b main origin/main
grep -q "^ARG APP_SHELL_VERSION=next$" frontend/Dockerfile || bad "X precondition: tree unexpectedly pinned"
export CORE=""
out=$(bash "$STEPS/release-qa--pin-frontend-versions.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "not pinned on this branch" && ok "X unpinned-shell refusal" || bad "X: rc=$rc $out"

echo "=== XI: snapshot guard trips on the real broken 3.7.0 pom ==="
# 3.7.0 shipped timestamp-locked snapshots of two modules; the guard exists
# because of exactly that content, so it is the canonical negative fixture.
git checkout -q .
git checkout -q 3.7.0 -- distro/pom.xml
out=$(bash "$STEPS/release-qa--refuse-snapshot-or-timestamp-locked-module-versions.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "locked snapshots is what broke 3.7.0" && ok "XI guard trips on real 3.7.0 snapshot versions" || bad "XI: rc=$rc $out"
git checkout -q HEAD -- distro/pom.xml

echo "=== XII: snapshot guard trips on a plain -SNAPSHOT module version ==="
sed -i -E 's|<fhir2.version>([^<]+)</fhir2.version>|<fhir2.version>\1-SNAPSHOT</fhir2.version>|' distro/pom.xml
grep -q -- "-SNAPSHOT</fhir2.version>" distro/pom.xml || bad "XII precondition: seed edit failed (fhir2.version property moved?)"
out=$(bash "$STEPS/release-qa--refuse-snapshot-or-timestamp-locked-module-versions.sh" 2>&1); rc=$?
[ $rc -ne 0 ] && echo "$out" | grep -q "locked snapshots is what broke 3.7.0" && ok "XII guard trips on -SNAPSHOT module version" || bad "XII: rc=$rc $out"
git checkout -q HEAD -- distro/pom.xml

echo ""
echo "=== RESULT: $PASS passed, $FAIL failed ==="
exit $FAIL
