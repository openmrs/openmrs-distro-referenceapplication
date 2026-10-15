# O3 Backend Modules on Core 3.x

This directory contains a script that builds, from source, every backend module of the O3 Reference Application against OpenMRS core 3.0.0-SNAPSHOT. It shows whether the modules compile, and optionally pass their own tests, on core 3.x. It also collects the resulting `.omod` files, ready to load into a core 3.x server.

## Files

- **`build-o3-core3-modules.sh`**: clones each module's 3.x branch, builds the modules one after another in dependency order, and collects the omods

## Quick Start

```bash
# Compile and package every module (about 75 minutes the first time)
./tests/core3-modules/build-o3-core3-modules.sh

# Also run every module's tests
RUN_TESTS=1 ./tests/core3-modules/build-o3-core3-modules.sh

# Put everything somewhere other than ./o3-core3-modules
./tests/core3-modules/build-o3-core3-modules.sh ~/o3-core3
```

## Where the Results Go

Everything the script writes goes under one work directory: `./o3-core3-modules`, relative to where you run it, or the folder you pass as its argument. To remove all of it, delete that folder.

| Path | Contents |
|------|----------|
| `omods/` | **The result**: one `<module>-<version>.omod` per module. Copy these into the `modules/` folder of an OpenMRS application data directory running core 3.0.0-SNAPSHOT. |
| `logs/<module>.log` | The Maven output of each module's build |
| `src/` | A shallow git clone of each module |
| `m2/` | The Maven repository the build uses |

The last lines of a run print the full path of the `omods/` folder. They also list the modules built from open pull requests (see below).

`omods/` is replaced only once every module has built and its omod has been collected. If a run stops before that, the previous run's omods stay there.

## Before You Run It

### Time

These figures were measured on an Apple Silicon Mac (macOS, JDK 21) on 14 October 2026:

| Run | Time |
|-----|------|
| First run, with an empty `m2/` | about 75 minutes |
| Later runs, with or without `RUN_TESTS=1` | about 45 minutes |

Two modules account for much of the time:
- metadatamapping takes about 16 to 17 minutes on its own. Its OWA build downloads Node 5.1.1 and npm 3.3.12 and runs `npm install` on an AngularJS app.
- On a first run, legacyui takes about 12 minutes, mostly downloading dependencies.

### Disk Space

A first run uses about 2.8 GB. With `RUN_TESTS=1`, test output brings it to about 3.1 GB.

| Path | Size |
|------|------|
| `src/` | 1.3 GB (1.6 GB with test output) |
| `m2/` | 1.3 GB |
| `omods/` | 132 MB |

`m2/` is reused on later runs, which is why they are faster. Deleting it costs you the download time again.

### Requirements

- git, Maven 3.9+, and JDK 21 or later. JDK 21 is the only version this was tested with: in CI, metadatamapping's OWA step has failed on JDK 25. The script checks the JDK Maven runs on, which is `JAVA_HOME` when that is set, not necessarily the `java` on your `PATH`.
- Network access to:
  - GitHub
  - mavenrepo.openmrs.org
  - Maven Central and Sonatype
  - nexus.mekomsolutions.net
  - repo.mybahmni.org
  - nodejs.org and the npm registry, for the OWA builds of bedmanagement, metadatamapping and openconceptlab
- No database, Docker or running OpenMRS. The tests use an in-memory H2 database.

### Your Maven Setup

- The script never reads from or writes to `~/.m2/repository`. A SNAPSHOT you installed there earlier can neither leak into this build nor be overwritten by it.
- Maven still reads `~/.m2/settings.xml`. A mirror or proxy configured there that cannot reach the repositories above will break the build.

### What It Builds

- Each module is built from the latest commit of the branch listed in the `MODULES` array at the top of the script. That is usually `core-3.x`, and `master` or `6.x` for a few. The results can therefore change from one day to the next as those branches move.
- The distro's `event` module is not built: it goes away on Platform 3 ([openmrs/openmrs-module-event#33](https://github.com/openmrs/openmrs-module-event/pull/33)), and none of these modules requires it.
- Three modules come from forks, because their 3.0 upgrade is still an open pull request. Each is pinned to the commit that was tested:

  | Module | Pull request |
  |--------|--------------|
  | initializer | [mekomsolutions/openmrs-module-initializer#338](https://github.com/mekomsolutions/openmrs-module-initializer/pull/338) |
  | appointments | [Bahmni/openmrs-module-appointments#213](https://github.com/Bahmni/openmrs-module-appointments/pull/213) |
  | teleconsultation | [Bahmni/openmrs-module-teleconsultation#13](https://github.com/Bahmni/openmrs-module-teleconsultation/pull/13) |

  Once one of these merges, point its line at the upstream branch and drop the commit.
- Running the script again resets every clone in `src/` to that commit. Any local changes there are discarded.

## Exit Status

- **0**: every module built and, with `RUN_TESTS=1`, every test passed.
- **1**, when a module fails to build: the run stops at that module, prints the Maven errors and names that module's log.
- **1**, with `RUN_TESTS=1`, when tests failed: the build carries on past them, still collects the omods and names the modules with failures. Search their logs for `FAILURE!`.
