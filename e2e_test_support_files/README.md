# Run E2E Tests on Release PRs

This GitHub Actions workflow, named "Run E2E Tests on Release PRs," serves as Quality Gate #4 in the slide
deck [O3 Release QA pipeline](https://docs.google.com/presentation/d/1k3DH74Mz1Afnrgy2MpwR5HQK5vMpVx0pTfN1na62lvI/edit#slide=id.g165af5ac0be_0_24).

The workflow is designed to automate the end-to-end (E2E) testing process for release pull requests (PRs)
that opened in the format described in
here: [How to Release the O3 RefApp](https://wiki.openmrs.org/display/projects/How+to+Release+the+O3+RefApp)

The workflow is conditional and **only runs if the pull request title starts with "(release)"**.

Below is an overview of the key components of the workflow:
<a href="https://ibb.co/g9GmpHL"><img src="https://i.ibb.co/MSbZ4Wx/Screenshot-2023-10-24-at-18-13-19.png" border="0"></a>

## Job: build

This job prepares the test environment, extracts version numbers, builds and runs Docker containers, and uploads
artifacts for use in subsequent E2E testing jobs.

### Checking out to the release commit

The reason for the step:

```yaml
- name: Checkout to the release commit
  run: git checkout 'HEAD^{/\(release\)}'
```

is to ensure that the workflow checks out to the specific release commit associated with the pull request. This is
necessary because the pull request contain both release commits and revert commits, and the goal is to specifically
target the release commit for further processing.

## End-to-End Test Jobs

The workflow includes several end-to-end test jobs, each corresponding to a specific components of O3 (frontend
mono-repos). These jobs are structured similarly and are listed below:

* `run-patient-management-e2e-tests`
* `run-patient-chart-e2e-tests`
* `run-form-builder-e2e-tests`
* `run-esm-core-e2e-tests`
* `run-cohort-builder-e2e-tests`

In each "End-to-End Test Job," the workflow first checks out the repository associated with a specific OpenMRS
component. It then downloads Docker images from a previous "build" job, loads these images, and starts an OpenMRS instance.

### Why Check Out to the Tags?

The workflow checks out a specific tagged version of the component's repository, the tag is imported from the previous "
build" job. This is necessary because the goal is to perform end-to-end tests on the codebase that corresponds to a
particular release version, rather than the code at the head of the repository. In case of using pre-releases, it checkouts
to the main branch as we don't create tags for pre-releases.
