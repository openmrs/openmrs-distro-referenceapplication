## What's Changed

### New features :rocket:

- (feat) O3-3218: Make the schema validation(block/allow) render a configuration by @gitcliff in https://github.com/openmrs/openmrs-esm-form-builder/pull/271
- (feat) O3-3341: Add the ability to restore deleted questions by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/283
- (feat) O3-3140: Add `patientIdentifier` question type with custom validator by @gitcliff in https://github.com/openmrs/openmrs-esm-form-builder/pull/264
- (feat) O3-3351: Add support for obs comments in the form builder inter… by @CynthiaKamau in https://github.com/openmrs/openmrs-esm-form-builder/pull/285
- (feat) O3-3355: Add support for inline date in the form builder intera… by @CynthiaKamau in https://github.com/openmrs/openmrs-esm-form-builder/pull/286
- (feat) O3-3357: Support editing person attributes in the Edit question modal by @gitcliff in https://github.com/openmrs/openmrs-esm-form-builder/pull/287
- (feat) O3-3368: Support editing encounterDatetime fields in the edit question modal by @gitcliff in https://github.com/openmrs/openmrs-esm-form-builder/pull/289
- (feat) O3-3405: Support creating time rendering questions using interactive builder by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/303
- (feat) O3-3342: Add support for program states in form builder by @CynthiaKamau in https://github.com/openmrs/openmrs-esm-form-builder/pull/284

### Bug fixes :bug:

- (fix) O3-3254: Remove trailing slash from uploadSchema request by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/275
- (fix) Set serialized schema blob MIME type to `application/json` by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/276
- (fix) O3-3272: Fix question deletion logic in the interactive builder by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/280
- (fix) O3-3271: Fix Question Position Changes on Edit in Form Builder by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/281
- (fix): Rename datePickerType prop to datePickerFormat by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/290
- (fix) Lift up showSchemaSaveWarning state by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/293
- (fix) Reorder fields in the Edit question modal by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/295
- (fix) Fixed program workflow question on edit mode by @CynthiaKamau in https://github.com/openmrs/openmrs-esm-form-builder/pull/301
- (fix) Fix styling of the program state tag container by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/305
- (fix) Use state uuid as opposed to concept uuid in program state inte… by @CynthiaKamau in https://github.com/openmrs/openmrs-esm-form-builder/pull/313
- (fix) Reset previous errors on schema update by @samuelmale in https://github.com/openmrs/openmrs-esm-form-builder/pull/316
- (fix) Fix modal header close button styles by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/317

### Tests :test_tube:

- (test) Add testing-related plugins to ESLint config by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/312

### Housekeeping :broom:

- (chore) Bump dependencies by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/274
- (chore) Bump react form engine by @samuelmale in https://github.com/openmrs/openmrs-esm-form-builder/pull/277
- (chore) Add Encounter Role type in the form builder question types. by @hadijahkyampeire in https://github.com/openmrs/openmrs-esm-form-builder/pull/278
- (chore) Fix `@carbon/react` version to 1.37.0 by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/282
- (chore) Bump turbo by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/291
- (chore): RFE version bump by @arodidev in https://github.com/openmrs/openmrs-esm-form-builder/pull/296
- (chore) Bump react form engine by @gitcliff in https://github.com/openmrs/openmrs-esm-form-builder/pull/292
- (chore) Bump core tooling by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/294
- (chore) Bump React Form Engine by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/298
- (chore) Bump react form engine by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/299
- (chore) Add remote caching to E2E workflow using turborepo-gh-artifacts by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/308
- (chore) Bump yarn and turbo by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/310
- (chore) Updates carbon dependency to match FE lib requirement by @pirupius in https://github.com/openmrs/openmrs-esm-form-builder/pull/309
- (chore) Bump React Form Engine library by @samuelmale in https://github.com/openmrs/openmrs-esm-form-builder/pull/315
- (chore) Update rfe and framework by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/314
- (chore) Bump ws from 7.5.9 to 7.5.10 by @dependabot in https://github.com/openmrs/openmrs-esm-form-builder/pull/307
- (chore) Bump @babel/traverse from 7.20.1 to 7.24.7 by @dependabot in https://github.com/openmrs/openmrs-esm-form-builder/pull/306

### Refactors :construction:

- (refactor) Render modals using the modal system by @NethmiRodrigo in https://github.com/openmrs/openmrs-esm-form-builder/pull/288
- (refactor) Refactor modal files to match naming conventions by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/300
- (refactor) Refactor modals to use the modal system by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/302

### Docs :book:

- (docs) Add steps on debugging E2E tests using Playwright Inspector by @denniskigen in https://github.com/openmrs/openmrs-esm-form-builder/pull/304

## New Contributors

- @gitcliff made their first contribution in https://github.com/openmrs/openmrs-esm-form-builder/pull/271
- @NethmiRodrigo made their first contribution in https://github.com/openmrs/openmrs-esm-form-builder/pull/281
- @CynthiaKamau made their first contribution in https://github.com/openmrs/openmrs-esm-form-builder/pull/285
- @dependabot made their first contribution in https://github.com/openmrs/openmrs-esm-form-builder/pull/307

**Full Changelog**: https://github.com/openmrs/openmrs-esm-form-builder/compare/v2.5.0...v2.6.0
