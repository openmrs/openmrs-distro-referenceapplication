# Release & Deploy Runbook

All jobs run under **Actions → Run workflow** (`workflow_dispatch`) unless noted. Every job is
gated to the `openmrs` org and requires the `DEPLOY_SSH_KEY` secret to be set.

## Environments

| Env   | Host              | Images tag | How it deploys                                              |
|-------|-------------------|------------|------------------------------------------------------------|
| dev3  | dev3.openmrs.org  | `dev3`     | Auto on every successful `main` image build; also manual. Also deploys sec3. |
| test3 | test3.openmrs.org | `qa`       | By *Cut QA Release Candidate*; also manual; weekly reset (Mon 01:00 UTC). |
| o3    | o3.openmrs.org    | `demo`     | By *Promote QA to Prod*; also manual; nightly reset (01:00 UTC). |

## Which job to run when

1. **Cut a release candidate** → `Release: Cut QA Release Candidate` (`release-qa.yml`).
   Builds RC images, deploys to **test3** for QA.
2. **QA passes** → `Release: Promote QA to Prod` (`release-prod.yml`).
   Finalizes the release, builds `demo` images, deploys to **o3**.
3. **QA fails** → commit fixes to `releases/<version>`, re-run job 1 for the next RC.
4. **Ad-hoc redeploy** → run `Deploy to Dev3` / `Deploy QA to test3` / `Deploy to O3` directly.

## `Release: Cut QA Release Candidate`

Cuts `releases/<version>` (first RC) or adds an RC to an existing branch.

Inputs:
- `release_version` (required) — `X.Y.Z`, e.g. `3.7.1`. Must not already be released.
- `core_version` — esm-core / app-shell version, e.g. `10.0.0`. **Required for the first RC**; omit on later RCs (kept from the branch).
- `base_ref` — ref to cut the first RC from (default `main`; ignored once the branch exists).
- `dry_run` — prepare the commit but push/build nothing.

Effect: pushes `releases/<version>` + tag `<version>-rc.N`, builds all images (`<version>-rc.N` and `qa`), deploys test3, runs release E2E, and opens a dev-version bump PR on `main`.

Follow-up:
1. Run the [QA checklist](https://om.rs/o3qasheet) on test3.
2. Fixes needed → commit to `releases/<version>`, re-run this job.
3. QA passed → run *Promote QA to Prod*.
4. Merge the auto-opened dev-version bump PR on `main`.

## `Release: Promote QA to Prod`

Promotes the latest QA'd RC on `releases/<version>` to the final release. Re-running after a
partial failure is safe (it resumes).

Inputs:
- `release_version` (required) — `X.Y.Z`; the `releases/<version>` branch and an RC tag must exist.
- `dry_run` — prepare the commit but push/build nothing.

Effect: pins frontend modules to the exact QA-tested versions, pushes tag `<version>`, builds all
images (`<version>` and `demo`), deploys o3, and runs release E2E (non-blocking).

Follow-up:
1. Verify images/module versions on o3 (see the `cosign verify` command in the run summary).
2. Announce the release in #openmrs3 and on OpenMRS Talk.

## Standalone deploy jobs

All take a single `reset` boolean (default `false`); `reset: true` destroys volumes (full wipe).

- **Deploy to Dev3** (`deploy-dev3.yml`) — deploys current `dev3` images to dev3 + sec3.
- **Deploy QA to test3** (`deploy-test3.yml`) — deploys current `qa` images to test3.
- **Deploy to O3** (`deploy-o3.yml`) — deploys current `demo` images to o3.

## Deprecated Profiles & Images

* **`no-demo`:** The `no-demo` Maven profile and its associated Docker images (`*-no-demo` tags) have been deprecated and entirely removed from the build process. They have been replaced by the new clean, default distro build which provides a stable baseline.
