#!/usr/bin/env bash
#
# Builds, from source, every backend module of the O3 Reference Application for
# OpenMRS core 3.0.0-SNAPSHOT, and collects the .omod files in one folder.
#
#   ./build-o3-core3-modules.sh [WORK_DIR]               compile and package only
#   RUN_TESTS=1 ./build-o3-core3-modules.sh [WORK_DIR]   also run every module's tests
#
# WORK_DIR defaults to ./o3-core3-modules. The omods end up in WORK_DIR/omods,
# and each module's Maven log in WORK_DIR/logs.
#
# Read README.md next to this script before running it: a first run takes
# over an hour and about 3 GB of disk, and it lists what else to expect.

set -euo pipefail

WORK_DIR="$(mkdir -p "${1:-o3-core3-modules}" && cd "${1:-o3-core3-modules}" && pwd)"
SRC="$WORK_DIR/src"
OUT="$WORK_DIR/omods"
M2="$WORK_DIR/m2"

# module | GitHub repository | branch [| commit]
# A branch on a fork is a 3.0 port that is still an open pull request; the
# comment names the PR. Its line pins the commit that was tested, so a later
# push to the branch does not change the build unnoticed. Point the line at the
# upstream branch, without a commit, once the PR merges.
#
# The list is in build order: every module comes after the modules it depends
# on, so a sibling SNAPSHOT it asks for is the one just built (the forks'
# SNAPSHOTs are not published at all). A module still builds against the
# versions its own POM declares, though: initializer's platform-3.0 branch
# declares the pre-3.0 releases of queue, billing, emrapi, appointments,
# addresshierarchy, openconceptlab, patientflags and tasks, so its build does
# not check it against the omods collected here.
MODULES=(
	"legacyui              | openmrs/openmrs-module-legacyui              | master"
	"addresshierarchy      | openmrs/openmrs-module-addresshierarchy      | core-3.x"
	"webservices.rest      | openmrs/openmrs-module-webservices.rest      | master"
	# Bahmni/openmrs-module-teleconsultation#13
	"teleconsultation      | dkayiwa/openmrs-module-teleconsultation      | platform-3.0 | c72f5ecb8476c4187f9a1ad562ad969797609bf1"
	# Bahmni/openmrs-module-appointments#213
	"appointments          | dkayiwa/openmrs-module-appointments          | platform-3.0 | f3238f30b3adb6e2eaafd320242a39afc1d83cda"
	"attachments           | openmrs/openmrs-module-attachments           | core-3.x"
	"authentication        | openmrs/openmrs-module-authentication        | core-3.x"
	"bedmanagement         | openmrs/openmrs-module-bedmanagement         | core-3.x"
	"fhir2                 | openmrs/openmrs-module-fhir2                 | 6.x"
	"idgen                 | openmrs/openmrs-module-idgen                 | core-3.x"
	"stockmanagement       | openmrs/openmrs-module-stockmanagement       | core-3.x"
	"billing               | openmrs/openmrs-module-billing               | core-3.x"
	"calculation           | openmrs/openmrs-module-calculation           | master"
	"cohort                | openmrs/openmrs-module-cohort                | core-3.x"
	"metadatamapping       | openmrs/openmrs-module-metadatamapping       | core-3.x"
	"htmlwidgets           | openmrs/openmrs-module-htmlwidgets           | master"
	"serialization.xstream | openmrs/openmrs-module-serialization.xstream | master"
	"reporting             | openmrs/openmrs-module-reporting             | core-3.x"
	"emrapi                | openmrs/openmrs-module-emrapi                | core-3.x"
	"openconceptlab        | openmrs/openmrs-module-openconceptlab        | core-3.x"
	"patientflags          | openmrs/openmrs-module-patientflags          | core-3.x"
	"queue                 | openmrs/openmrs-module-queue                 | core-3.x"
	"tasks                 | openmrs/openmrs-module-tasks                 | core-3.x"
	# mekomsolutions/openmrs-module-initializer#338
	"initializer           | dkayiwa/openmrs-module-initializer-1         | platform-3.0 | b5e7ebd6e1fb83823b9b17bd5bf11c7765bedfc8"
	"metadataexport        | openmrs/openmrs-module-metadataexport        | core-3.x"
	"o3forms               | openmrs/openmrs-module-o3forms               | core-3.x"
	"ordertemplates        | openmrs/openmrs-module-ordertemplates        | core-3.x"
	"patientdocuments      | openmrs/openmrs-module-patientdocuments      | core-3.x"
	"referencedemodata     | openmrs/openmrs-module-referencedemodata     | core-3.x"
	"reportingrest         | openmrs/openmrs-module-reportingrest         | core-3.x"
)

die() { echo "ERROR: $*" >&2; exit 1; }

for tool in git mvn java; do
	command -v "$tool" >/dev/null || die "$tool is not on the PATH"
done
# Ask Maven which JDK it runs on: it uses JAVA_HOME when that is set, which
# need not be the java on the PATH.
java_major=$(mvn -v 2>&1 | sed -n 's/^Java version: \([0-9]*\).*/\1/p')
[ "${java_major:-0}" -ge 21 ] || die "JDK 21 or later is required; Maven runs on ${java_major:-an unknown JDK} (check JAVA_HOME)"

mkdir -p "$SRC" "$M2"

echo "== Fetching sources into $SRC"
names=()
pinned=()
for line in "${MODULES[@]}"; do
	IFS='|' read -r name repo branch commit <<<"$line"
	name=$(echo $name); repo=$(echo $repo); branch=$(echo $branch); commit=$(echo ${commit:-})
	names+=("$name")
	dir="$SRC/$name"
	if [ ! -d "$dir/.git" ]; then
		git init --quiet "$dir"
		git -C "$dir" remote add origin "https://github.com/$repo.git"
	fi
	git -C "$dir" remote set-url origin "https://github.com/$repo.git"
	git -C "$dir" fetch --quiet --depth 1 origin "${commit:-$branch}"
	git -C "$dir" checkout --quiet --force --detach FETCH_HEAD
	git -C "$dir" clean --quiet -fdx
	printf '  %-22s %s@%s %s\n' "$name" "$repo" "$branch" "$(git -C "$dir" rev-parse --short HEAD)"
	if [ -n "$commit" ]; then
		pinned+=("$name")
		head=$(git ls-remote "https://github.com/$repo.git" "refs/heads/$branch" | cut -f1)
		[ "$head" = "$commit" ] || echo "     note: $branch has moved on to ${head:0:7}; this builds the pinned ${commit:0:7}"
	fi
done

# Each module is built on its own, not under one aggregator POM: fhir2, o3forms
# and queue resolve files from the root of the Maven build, which an aggregator
# would move.
mvn_args=(-B -U -Dmaven.repo.local="$M2" install)
if [ "${RUN_TESTS:-0}" = 1 ]; then
	mvn_args+=(-Dmaven.test.failure.ignore=true)
else
	mvn_args+=(-DskipTests)
fi
mkdir -p "$WORK_DIR/logs"

echo "== Building (mvn ${mvn_args[*]}); the logs are in $WORK_DIR/logs"
failing_tests=()
for name in "${names[@]}"; do
	log="$WORK_DIR/logs/$name.log"
	printf '  %-22s ' "$name"
	start=$(date +%s)
	if ! mvn "${mvn_args[@]}" -f "$SRC/$name/pom.xml" >"$log" 2>&1; then
		echo "FAILED"
		grep -E '^\[ERROR\]' "$log" | head -40 >&2 || true
		die "$name failed to build; see $log"
	fi
	tests=""
	if [ "${RUN_TESTS:-0}" = 1 ]; then
		# Sum surefire's and failsafe's per-module totals (the lines without
		# "Time elapsed"; those are per test class).
		tests=$({ grep -E 'Tests run: [0-9]+, Failures: [0-9]+, Errors: [0-9]+, Skipped: [0-9]+ *$' "$log" || true; } |
			sed -E 's/.*Tests run: ([0-9]+), Failures: ([0-9]+), Errors: ([0-9]+), Skipped: ([0-9]+) *$/\1 \2 \3 \4/' |
			awk '{r+=$1; f+=$2; e+=$3; s+=$4} END {printf "%d %d %d %d", r, f, e, s}')
		read -r run failed errors skipped <<<"$tests"
		tests=", tests: $run run, $failed failed, $errors errors, $skipped skipped"
		[ $((failed + errors)) -eq 0 ] || failing_tests+=("$name")
	fi
	echo "ok ($(( $(date +%s) - start ))s$tests)"
done

echo "== Collecting the omods into $OUT"
# Collect into a fresh folder and swap it in only once every omod is there, so
# a failure here leaves the previous complete set in place.
rm -rf "$OUT.new"
mkdir -p "$OUT.new"
for name in "${names[@]}"; do
	# Only omod/target: test resources elsewhere under target/ include other
	# modules' .omod files (initializer's exti18n, rest's mockModule).
	found=("$SRC/$name"/omod/target/*.omod)
	[ -f "${found[0]}" ] && [ ${#found[@]} -eq 1 ] || die "expected one .omod in $SRC/$name/omod/target, found: ${found[*]}"
	cp "${found[0]}" "$OUT.new/"
done
rm -rf "$OUT"
mv "$OUT.new" "$OUT"
ls -1 "$OUT"
echo "== $(ls -1 "$OUT" | wc -l | tr -d ' ') omods in $OUT. Copy them into your OpenMRS application data directory's modules/ folder."
[ ${#pinned[@]} -eq 0 ] || echo "== Built from open pull requests, not from a merged upstream branch: ${pinned[*]}"
if [ ${#failing_tests[@]} -gt 0 ]; then
	echo "== Tests failed in: ${failing_tests[*]}. Search their logs in $WORK_DIR/logs for 'FAILURE!'." >&2
	exit 1
fi
