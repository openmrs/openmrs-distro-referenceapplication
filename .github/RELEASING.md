# Releasing the O3 Reference Application

Releases are driven by two GitHub Actions workflows (Actions tab → run with
**Run workflow**), which replace the former Bamboo plans:

| Step | Workflow | Replaces (Bamboo) |
|---|---|---|
| Cut an RC + publish QA images | **Release: Cut QA (RC)** (`release-qa.yml`) | O3-CQR, O3-CUQR, O3-PQR |
| Finalize + publish production images | **Release: Promote to Production** (`release-promote.yml`) | "Deploy Reference Application 3.x" (build half) |

## Release runbook

1. **Announce** the upcoming release in `#openmrs3`.
2. **Release modules first.** No `-SNAPSHOT` (or timestamp-locked snapshot)
   versions may remain in `distro/pom.xml` — the RC workflow refuses to run
   otherwise. Frontend modules are picked up automatically via their `latest`
   npm dist-tags.
3. **Run "Release: Cut QA (RC)"** with:
   - `release_version`: e.g. `3.7.1`
   - `core_version`: the [latest esm-core release](https://github.com/openmrs/openmrs-esm-core/releases), e.g. `10.0.0`
   - `base_ref`: `main` (default), or a tag like `3.7.0` for a surgical patch release
   - Tip: run once with `dry_run` checked and review the diff in the run summary first.

   First run for a version: cuts `releases/<version>` off `base_ref`, commits
   the version pins (`next` → `latest` for frontend modules, the app shell
   version, the poms), tags `<version>-rc.1`, and dispatches the image builds
   which publish `:<version>-rc.1` and `:qa` Docker images. Re-running for the
   same version cuts `rc.2`, `rc.3`, … from the release branch (commit fixes
   to the branch in between).
4. **QA on test3.openmrs.org** once it runs the new `qa` images
   (container refresh is not yet ported — see gaps below).
   Work through the [QA checklist](https://om.rs/o3qasheet).
5. **Run "Release: Promote to Production"** with the `release_version`.
   It pins the frontend to the *exact* module versions QA tested (extracted
   from the RC image's importmap), tags the final version, and publishes
   `:<version>`, `:<major.minor.x>` and `:demo` images. It refuses to promote
   if commits landed on the release branch after the last RC.
6. **Announce** the release in `#openmrs3` and on OpenMRS Talk.

`main` is never pushed to by these workflows. When a (minor) release makes the
development version on main stale, the RC workflow opens a version-bump PR —
review and merge it like any other PR.

## Not (yet) ported

- **Server container refreshes**: test3.openmrs.org (`qa` images) and
  o3.openmrs.org (`demo` images) are still refreshed by the OpenMRS
  infrastructure (Bamboo deploy steps). The `deploy-dev3.yml` scoped-SSH
  pattern can be extended to them once infrastructure provisions deploy keys.
- Bamboo remains available as a fallback; these workflows use the same
  branch/tag/commit conventions it did.
