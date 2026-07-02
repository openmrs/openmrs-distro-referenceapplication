# Releasing the O3 Reference Application

Releases are driven by two GitHub Actions workflows (Actions tab → run with
**Run workflow**), which replace the former Bamboo plans:

| Step | Workflow | Replaces (Bamboo) |
|---|---|---|
| Cut an RC + publish QA images | **Release: Cut QA (RC)** (`release-qa.yml`) | O3-CQR, O3-CUQR, O3-PQR (image-publish half) |
| Finalize + publish production images | **Release: Promote to Production** (`release-promote.yml`) | "Deploy Reference Application 3.x" (build half) |

The *server deployment* halves have their own workflows
(`deploy-test3.yml`, `deploy-o3.yml`) that activate once infrastructure
provisions their secrets — see "Not (yet) ported" below.

## Release runbook

1. **Announce** the upcoming release in `#openmrs3`.
2. **Release modules first.** No `-SNAPSHOT` (or timestamp-locked snapshot)
   versions may remain in `distro/pom.xml` — the RC workflow refuses to run
   otherwise. Frontend modules are picked up automatically via their `latest`
   npm dist-tags.
3. **Run "Release: Cut QA (RC)"** with:
   - `release_version`: e.g. `3.7.1`
   - `core_version`: the [latest esm-core release](https://github.com/openmrs/openmrs-esm-core/releases),
     e.g. `10.0.0`. Required for the first RC; leave empty on later RCs to
     keep the app shell QA has been testing (pass it again only to deliberately
     change the shell mid-release).
   - `base_ref`: `main` (default), or a tag like `3.7.0` for a surgical patch release
   - Tip: run once with `dry_run` checked and review the diff in the run summary first.

   First run for a version: cuts `releases/<version>` off `base_ref`, commits
   the version pins (`next` → `latest` for frontend modules, the app shell
   version, the poms), tags `<version>-rc.1`, and dispatches the image builds
   which publish `:<version>-rc.1` and `:qa` Docker images, plus the E2E test
   workflow against the release branch. Re-running for the same version cuts
   `rc.2`, `rc.3`, … from the release branch (commit fixes to the branch in
   between).

   **Recovery:** a re-run after a failure *before the RC tag was pushed*
   resumes safely (completed steps are skipped). After the tag is pushed,
   re-running cuts the next RC instead — so if only an image-build or E2E
   dispatch failed, dispatch those workflows directly against the RC tag
   (the exact commands are printed in the run summary; using the tag, not
   the branch, guarantees the images match the tagged commit even if the
   branch has moved on). Note that re-dispatching the backend re-deploys
   the same release version to the Maven repository; if the repository
   rejects re-deployment, cut the next RC instead.
4. **QA on test3.openmrs.org** once it runs the new `qa` images. The
   **Release: Deploy QA to test3** workflow (`deploy-test3.yml`) refreshes
   the server — automatically once all three RC image builds have
   published, or by manual dispatch. It requires infrastructure to
   provision `TEST3_DEPLOY_SSH_KEY` and `TEST3_SERVER_HOST` as
   **repository- or organization-level** secrets (environment-scoped
   secrets are invisible to the gate job; and the names are deliberately
   not dev3's `DEPLOY_SSH_KEY` / `SERVER_HOST`, which this repo already
   inherits and which point at the wrong server). Until then automatic
   runs no-op with a warning and manual dispatches fail with
   instructions; the fallback is the Bamboo
   [Publish QA Release](https://ci.openmrs.org/browse/O3-PQR) deploy step
   or `#infrastructure`. Then work through the
   [QA checklist](https://om.rs/o3qasheet).
5. **Run "Release: Promote to Production"** with the `release_version`.
   It pins the frontend to the *exact* module versions QA tested (from the
   resolved-version manifest baked into the RC image by
   `openmrs assemble --manifest`), tags the final version, and publishes
   `:<version>` and `:demo` images. It refuses to promote if commits landed
   on the release branch after the last RC, and re-running it after a partial
   failure resumes safely (including re-dispatching the image builds).
6. **Deploy to production**: dispatch **Release: Deploy to o3
   (production)** with the `release_version` once the promote image builds
   finish. It verifies per image that the `:demo` Docker Hub digest equals
   the `:<version>` digest before SSHing, and fails with instructions until
   the `O3_DEPLOY_SSH_KEY` / `O3_SERVER_HOST` repo- or org-level secrets
   are provisioned — until then use the Bamboo
   [Deploy Reference Application 3.x](https://ci.openmrs.org/deploy/viewDeploymentProjectEnvironments.action?id=222593025)
   project or `#infrastructure`.
7. **Announce** the release in `#openmrs3` and on OpenMRS Talk.

`main` is never pushed to by these workflows. When a (minor) release makes the
development version on main stale, the RC workflow opens a version-bump PR —
review and merge it like any other PR.

## Not (yet) ported

- **Server container refreshes**: both environments have workflows that
  activate once infrastructure provisions their secrets (repo- or
  org-level; environment-scoped secrets are invisible to the gate jobs):
  - test3.openmrs.org — `deploy-test3.yml`, `TEST3_DEPLOY_SSH_KEY` /
    `TEST3_SERVER_HOST` (target default `emr-3-test`). Automatic after RC
    builds; manual dispatch supported.
  - o3.openmrs.org — `deploy-o3.yml`, `O3_DEPLOY_SSH_KEY` /
    `O3_SERVER_HOST` (target default `emr-3-demo`). **Manual dispatch
    only** (production stays human-initiated); verifies the `:demo`
    Docker Hub digests match the promoted version before deploying.
    Consider adding required reviewers to the `o3` environment for a
    second approval gate.

  Until provisioned, the Bamboo fallbacks are
  [Publish QA Release](https://ci.openmrs.org/browse/O3-PQR) and
  [Deploy Reference Application 3.x](https://ci.openmrs.org/deploy/viewDeploymentProjectEnvironments.action?id=222593025),
  or `#infrastructure`.
- Bamboo remains available as a fallback; these workflows use the same
  branch/tag/commit conventions it did.

## Bamboo decommission checklist

Each item has its own precondition — after all five, nothing in the
release path needs Bamboo:

- Item 1: retirable now (`release-qa.yml` already cut a real release's RCs).
- Item 2: after `TEST3_*` secrets are provisioned and `deploy-test3.yml`
  has one successful deploy.
- Item 3: after `O3_*` secrets are provisioned and `deploy-o3.yml` has one
  successful deploy.
- Item 4: as soon as the schedule trigger is on main (it uses the same
  Docker Hub credentials every build already uses — nothing new to
  provision).
- Item 5: independent — confirm with infrastructure at any time.

To retire:

1. O3-CQR (Create initial QA release) and O3-CUQR (Create Updated QA
   Release) — replaced by `release-qa.yml`.
2. O3-PQR (Publish QA Release) — image publishing replaced by the build
   dispatches in `release-qa.yml`; the test3 deploy by `deploy-test3.yml`.
3. Deploy Reference Application 3.x — builds replaced by
   `release-promote.yml`; the o3 deploy by `deploy-o3.yml`.
4. The Bamboo cron that dispatched Build Frontend several times a day —
   replaced by the `schedule` trigger in `build-frontend.yml`.
5. Any legacy tag-watching triggers on this repo (the old "Distribution
   3.x Releases" project) — confirm with infrastructure nothing still
   fires on pushed tags.

## Testing changes to these workflows

`tests/release-workflows/run-suite.sh` executes the workflows' actual `run:`
blocks against an isolated local clone — the full release state machine plus
every guard, including negative fixtures seeded with the real content that
broke 3.7.0. It runs automatically on PRs touching the release workflows
(`release-workflow-tests.yml`) and locally on Linux with
`bash tests/release-workflows/run-suite.sh` (on macOS, run it inside a Linux
container).

## Known gaps and caveats

- **Backend rebuild at promote is not fully hermetic** (same as the Bamboo
  flow was): the root pom uses SNAPSHOT versions of the
  openmrs-sdk/packager Maven plugins and the Docker build starts from
  floating `openmrs/openmrs-core:2.8.x-*` base images, all re-resolved at
  build time. Promote soon after QA sign-off to keep that window small; a
  retag-based promote (shipping the QA-tested image bits unchanged) is the
  eventual fix. The *frontend module* versions are exact-pinned and immune
  to this.
- **E2E signal is advisory**: the dispatched E2E workflow checks out the
  esm test suites at `main`, so suites may test unreleased (`next`) behavior
  against the `latest`-pinned RC. This matches the coverage the old
  push-to-main flow had. Version-matched suites
  (`tests/e2e/extract_tag_numbers.sh`, used by the manually-disabled
  `e2e-on-release.yml`) are a follow-up.
- **Old-tag base refs**: cutting a patch from a tag older than the current
  workflow set runs the *build/E2E workflow files as of that tag* — they may
  lack dispatch inputs or cosign signing. Cherry-pick the current workflow
  files onto the release branch first if needed.
- **CI on the dev-version bump PR** starts automatically only when the
  `OMRS_BOT_GH_TOKEN` secret is available (it normally is — the dependency
  update workflow uses it too). If checks are missing, close and reopen
  the PR.
