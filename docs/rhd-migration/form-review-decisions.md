# RHD Form Review — Decision Log

Started 2026-10-05. This log documents every form-by-form comparison between the
live ACT registry (https://multi.stage.actregistry.org) and our OpenMRS distro
(local docker instance, test patient rhd00005 / "Test Logan Field"), along with
every fix applied and the reasoning behind each decision made without stopping
for review. Read top to bottom; each form gets its own section.

## Infrastructure blocker found and fixed: reporting module Spring context failure

While restarting the backend to verify the form changes above, discovered
that **Initializer had stopped running entirely** on recent restarts (no new
entries in `/openmrs/data/initializer.log` across 2 restarts, despite new
concepts being added to the CSVs). Root cause: the `reporting` module's
`ReportLoader` was throwing
`com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException:
Unrecognized field "uuid" (class
org.openmrs.module.reporting.config.DesignDescriptor)` while parsing
`designs:` blocks in our own report YAML files
(`distro/configuration/reports/reportdescriptors/**/*.yml`), which aborted
`performWebStartOfModules` before Initializer (which starts after `reporting`
in the module sequence) got its turn.

This predates this session's form work entirely -- these report YAML files
were not touched by anything in this review -- but it is a real, confirmed
regression affecting every restart since whenever these report files were
added or last changed, and it was silently blocking every config reload,
not just this session's.

**Fix:** confirmed via the `openmrs-module-reporting` source
(`ReportDescriptor.java` has a valid top-level `uuid` field; its nested
`DesignDescriptor` class does not) that each report YAML's `designs:` entry
had a `uuid:` sub-field that `DesignDescriptor` doesn't recognize. Removed
exactly that one line from each of the 8 affected files (confirmed via diff
that only the nested `designs[].uuid` line was removed in each file, every
top-level report `uuid` field was left untouched):
`rhd_patients.yml`, `screen_positive_pending.yml`, `rhd_visits.yml`,
`procedural_waiting_list.yml`, `rhd_screening_cascade.yml`,
`rhd_care_cascade.yml`, `rhd_encounters.yml`, `inr_monitoring.yml`.

**Caught a mistake mid-fix**: a first pass used a regex that matched both the
valid top-level `uuid:` and the invalid nested one, silently deleting 8
legitimate report-identity UUIDs. Caught it by checking `git diff` right
after and noticing two `uuid:` lines removed per file instead of one;
reverted with `git checkout --` and redid the fix with an indentation/context
-aware line-by-line pass instead of a blanket regex.

---

## Post-restart verification cycle: bugs found and fixed in the fixes themselves

After the reporting-module YAML fix unblocked Initializer, several restarts
surfaced real problems in this session's own edits -- the kind of
cross-reference bugs that only show up once Initializer actually tries to
load everything together. Documenting each since they represent real
corrections, not just notes.

**1. Renaming a form doesn't update it in place.** Renaming "RHD Consent" to
"RHD Research Participation" in the JSON caused Initializer's Ampath Forms
loader to compute a new deterministic UUID from the new name and create an
entirely new form row (form_id 18), leaving the old "RHD Consent" row
(form_id 8) orphaned and still active. Tried adding an explicit `"uuid"`
field to the JSON pointing at the old form's UUID, hoping Initializer would
honor it as an override and update in place -- **confirmed this does not
work**: the old form was untouched across a restart, the new one loaded
under its new deterministic UUID regardless. Removed the no-op `uuid` field.
**Known limitation, not fixed**: the old "RHD Consent" form (form_id 8,
retired=0) remains in the database, duplicating "RHD Research Participation"
(form_id 18) in the Clinical Forms list. A direct SQL retirement was
available but a destructive-adjacent direct-DB write was correctly declined
by the safety classifier; the config-driven path (a dedicated
forms-retirement CSV domain) does not exist in this distro's Initializer
setup. **Flagged for manual cleanup** -- either retire form_id 8 by hand via
the OpenMRS administration UI, or find/add the right Initializer-supported
mechanism for retiring a form whose name changed.

**2. Renamed/voided concepts broke other, unrelated usages of the same
concept.** Several fixes this session renamed a concept's FSN or voided it
assuming it was only used in the one place being fixed -- in a few cases
that assumption was wrong:
- **"Mitral Stenosis" and "Pulmonary Hypertension"**: renaming these 2
  Echocardiogram question concepts' FSNs to match live exactly collided
  (case-insensitively) with pre-existing, differently-purposed concepts
  already in the dictionary (a CIEL diagnosis concept "Mitral stenosis", and
  CIEL concept 128125AAAA "Pulmonary Hypertension"). OpenMRS enforces
  concept-name uniqueness per locale regardless of concept class, so a
  Coded question and a Diagnosis-class answer can't share a display name.
  **Fix**: reverted the FSN to a disambiguated form ("Mitral stenosis
  severity (RHD)", "Pulmonary hypertension severity (RHD)") while keeping
  the *form label* as "Mitral Stenosis"/"Pulmonary Hypertension" -- the form
  label and the concept's own name are independent, so live-matching display
  text didn't require a live-matching internal name. Also updated the one
  conceptset that referenced the old bare FSNs by name.
- **"Cause of Death"**: the new Patient-Information-specific concept
  collided with a pre-existing CIEL concept "Cause of death". Same fix
  pattern -- FSN disambiguated to "Cause of Death (Patient Registry)", form
  label stays "Cause of Death", conceptset reference updated.
- **"1"/"2"/"3" (Number of Fetuses)**: collided with the pre-existing RACHs
  Score "1"/"2"/"3" concepts (deliberately avoided reusing those earlier,
  but still collided on bare name since I gave the new concepts the same
  bare FSN). Fixed by disambiguating to "Number of Fetuses: 1/2/3", form
  labels stay the bare "1"/"2"/"3".
- **"Active"/"Inactive" (Patient Information's Status field)**: these answer
  options were initially implemented by reusing the Research Participation
  form's "Active (study status)"/"Inactive - Death" concepts -- wrong reuse
  (different semantics: one is a generic patient status, the other a
  study-enrollment status) and also collided on bare "Inactive" with another
  pre-existing concept. **Fix**: minted 2 new, correctly-scoped concepts
  ("Active (Patient Registry)", "Inactive (Patient Registry)") and repointed
  the form JSON's answers and its `hideWhenExpression` to the new UUIDs.
- **"Acute rheumatic fever"**: voided this concept when reworking Category
  at Diagnosis, without checking it was *also* an active answer for the
  pre-existing "RHD Complications" field elsewhere in Consultation Visit.
  Voiding it broke that unrelated field's conceptset load. **Fix**:
  un-voided the concept (restored its description to explain both usages);
  the Category at Diagnosis fix itself only needed to stop *using* it as an
  answer there, which the form JSON already did correctly -- voiding the
  shared concept was the unnecessary, over-broad part.
- **Bare "Stroke" references** (3 occurrences across Pregnancy and Patient
  Information CSVs) were left unresolved as plain text instead of explicit
  UUIDs; "Stroke" is ambiguous (a CIEL diagnosis concept vs. this project's
  own custom Stroke concept already used elsewhere). Fixed all 3 to use the
  explicit UUID of this project's established Stroke concept
  (41b1a801-9d9d-5687-9de5-39e89c73c8c7).
- **"Once a day"/"Twice a day"** inside a *voided* Oral Adherence concept row
  were also ambiguous (Frequency-class CIEL concepts vs. this project's Misc
  -class concepts already used by the active Dosing Interval field) --
  fixed to explicit UUIDs even though the row is voided, since Initializer
  still appears to process a voided row's own answer-list during load.

**3. Voided conceptset members must be voided at the conceptset level too.**
Every conceptset whose members were voided this session (Allergies (RHD
section), Anticoagulation (RHD section), Anticoagulation monitoring
(Anticoagnulation Monitoring) (RHD section), Cardiac intervention (RHD
section), Chronic health conditions (RHD section), RHD consultation update
(RHD section) -- 6 total) initially tried to keep the conceptset itself
*active* with a "kept for historical reference" note, on the assumption that
was harmless. It wasn't: Initializer cannot resolve a voided concept by bare
name when loading an active conceptset's member list, so every one of these
threw a load error on restart (surfacing as cascading "could not be found in
database" errors against whatever conceptset happened to be processed next
in the same pass). **Fix**: voided all 6 conceptsets themselves, not just
their members, updating each description to explain why historical-reference
framing wasn't sufficient.

**4. Conceptsets referencing renamed concepts by their old bare name.**
Three conceptset member lists still used a concept's pre-rename FSN after
the concept itself was renamed this session (`Indication` ->
`Indication (INR Monitoring)`, `Date Tested` -> `Date Tested (INR
Monitoring)`, `INR Value` -> the CIEL concept's real name `INR`). Updated all
3 references.

**5. Echocardiogram conceptset referencing a retired concept by its old bare
name.** A separate, earlier session (2026-09-10) had already retired "Summary
of Last Echo (DD-Month-YYYY)" in favor of a real date field, "Date of
Echocardiogram" -- but never updated the Echocardiogram (RHD section)
conceptset's member list, which still referenced the retired concept's old
bare name. Same failure mode as issue 3 above (Initializer can't resolve a
retired/voided concept by bare name in an active conceptset), just discovered
later because this conceptset's load error kept reporting a *different*
member as "identified by" across 2 earlier fix attempts (the Mitral/Aortic
Regurgitation collisions, items already listed above), masking this one until
those were cleared. **Fix**: changed the member list entry from "Summary of
Last Echo (DD-Month-YYYY)" to "Date of Echocardiogram".

**Pre-existing, unrelated errors confirmed NOT caused by this session**
(verified by diffing against the initializer.log entries from the very first
restart of this session, before any of this session's edits existed):
"Electrocardigram" (sic, pre-existing typo'd concept name), "Next
Consultation", "Primary Surgeon/Cardiologist", "Height" (Vital signs
conceptset), and "Gestational Age (in weeks)" (this one looks like it should
be new given the Pregnancy rework, but the identical failure was already
present in the very first pre-session log, confirming it predates this
review). Left these alone; out of scope for this pass.

**Final verification (2026-10-05, after the restart that picked up fix #5
above): clean.** Confirmed via a fresh `initializer.log` error-summary
segment and direct DB queries:
- Zero `DuplicateConceptNameException` / "Multiple concepts" errors.
- Zero "could not be found in database" errors for anything this session
  touched (Echocardiogram's conceptset now loads with no error at all).
- Remaining `ERROR` lines in the log are exactly the 2 known pre-existing,
  unrelated ones (`SOAP Note Template`, `Test Form 1` -- demo forms
  referenced by report descriptors that don't exist in this distro).
- All 6 retired forms confirmed `retired=1` in the database: RHD Allergies,
  RHD Anticoagulation, RHD Anticoagulation Monitoring, RHD Cardiac
  Intervention, RHD Chronic Health Conditions, RHD Consultation Update.
  (RHD Interventional Recommendations was retired/handled earlier in the
  session, see its own section below.)
- Spot-checked 6 disambiguated concepts, all present, not retired, not
  voided: Mitral stenosis severity (RHD), Pulmonary hypertension severity
  (RHD), Cause of Death (Patient Registry), Active (Patient Registry),
  Inactive (Patient Registry), Date of Echocardiogram.
- **Remaining known limitation (unchanged from issue 1 above)**: "RHD
  Consent" (form_id 8, retired=0) still exists in the database alongside
  "RHD Research Participation" (form_id 18), duplicating it in the Clinical
  Forms list. Needs manual retirement via the OpenMRS admin UI, or a
  config-driven mechanism this distro doesn't currently have. This is the
  one open item from the entire session.

---

## TL;DR for reviewers

**5 forms retired as orphaned duplicates** (wrong encounter type, superseded
by a repeating entry already embedded in Consultation Visit, or simply
fabricated with no live counterpart): RHD Interventional Recommendations,
RHD Allergies, RHD Anticoagulation, RHD Anticoagulation Monitoring, RHD
Chronic Health Conditions, RHD Consultation Update (6 total -- see each
form's section for which).

**1 form renamed and entirely rebuilt**: RHD Consent -> RHD Research
Participation (the old content, consent-giver tracking, had no live
equivalent at all; replaced with live's actual Study ID/Enrollment/Completion/
Status structure).

**Forms with substantial field-level rework**: RHD Oral Adherence, RHD INR
Monitoring, RHD Patient Information, RHD Pregnancy (multiselect-to-single-
select correctness bug fixed here), the embedded Interventional
Recommendations/Allergies/Chronic Health Condition sections inside RHD
Consultation Visit.

**Forms with small label-only fixes**: RHD Hospital Admission (plus one
reversal of a prior session's Facility-picker decision -- live's Hospital
Admission Facility is plain text, unlike BPG's), RHD Echocardiogram, RHD
Electrocardiogram.

**Spot-checked and trusted rather than re-audited**: RHD Procedures and
Outcomes (56 fields, already had detailed prior live-verification notes from
an earlier session).

**Known gaps, flagged but not fixed** (follow-up candidates): Patient
Information's Diagnosis is a flat field, not live's repeating
Category/Details/Priority/Active grid.

Every void/retirement was confirmed against the database first (`SELECT
COUNT(*) FROM obs WHERE concept_id = ...`) to ensure zero patient data would
be affected -- nothing in this pass touches or risks existing patient
observations.

Conventions:
- "Live" = the ACT registry (source of truth for field behavior/options).
- "Distro" = this repo's `distro/configuration/ampathforms/*.json` + concept CSVs.
- Decisions are made unilaterally per instruction; flagged for later review here.

---

## RHD Interventional Recommendations (completed earlier this session)

**Finding:** Two competing implementations existed:
1. Standalone `rhd_recommendations.json` — orphaned, flat/non-repeating, wrong
   encounter type (`RHD Consultation Visit`), far fewer options (3 vs 22/18),
   no Urgency field. Exposed to users in Clinical Forms list.
2. Embedded repeating obsGroup in `rhd_consultation.json`'s "Plan and follow-up"
   section (`interventionalRecommendationEntry`) — correct structure, matches
   live's repeating "Procedural Recommendation" grid almost exactly.

**Live verified field set** (via live patient rhd00005's Consultation Visit →
Procedural Recommendation → Edit dialog):
- Procedure Type: Catheterization / Surgery (single-select)
- Procedural Recommendation Name: 22 surgery options / 18 catheterization
  options (conditional on Procedure Type), free text allowed
- Urgency: "1: Emergent (24 hours)", "2: Urgent (60 days/2 months)",
  "3: Elective (180 days / 6 months)" — 3 options
- Completed: Yes/No, always visible

**Decisions:**
1. Fix Urgency in `rhd_consultation.json`'s embedded obsGroup to match live's
   3-tier set exactly (was a wrong 4-tier week/month scheme).
2. Retire the standalone `rhd_recommendations.json` form (set `retired: true`
   equivalent / remove from Clinical Forms exposure) since it's superseded by
   the embedded version and actively misleading if opened.
3. Keep our "Anomalous pulmonary vein repair" spelling (correct) over live's
   "Anamolous" (typo) — do not reproduce a source typo.

**Executed:**
- `rhd_consultation.json`: fixed Urgency option labels to "2: Urgent (60 days/2 months)"
  and "3: Elective (180 days / 6 months)" (the correct 3-tier answer concepts
  already existed in `rhd_answers_procedures.csv` and were already wired as the
  active, non-hidden answers in the form; only the label text needed a tweak).
  The old 4-tier week/month answers remain in the JSON with `isHidden: true`
  (pre-existing choice, left as-is -- likely kept for backwards compat with any
  historical obs using those concepts; harmless since hidden from the picker).
- `rhd_recommendations.json`: set `retired: true`, added a description note
  explaining why, to remove it from the Clinical Forms list.

---

## RHD Hospital Admission (revisited)

**Important reversal:** a prior session applied the `location_datasource` /
`ui-select-extended` picker to Hospital Admission's "Facility" field under the
assumption that live's Facility field should behave like BPG Delivery's
(which *is* a real picklist there). Opening live's actual Hospital Admission
form this session (fresh/blank, patient rhd00005) showed **Facility is a
plain free-text input on live for this specific form** -- confirmed via direct
DOM inspection (`<input type="text" name="facility">`, no `role=combobox`,
no react-select). Live is inconsistent between its own forms here (BPG uses a
fixed picklist, Hospital Admission uses free text for the same concept name),
but the task is to match each form's live behavior, not to impose consistency
live itself doesn't have.

**Decision:** reverted Hospital Admission's Facility back to plain
`"rendering": "text"`, dropping the `location_datasource` config. BPG
Delivery's Facility picker is untouched (confirmed correct against live
earlier this session's prior work).

**Other live-verified mismatches fixed:**
- "Reason for Admission" answer labels: live uses "RHD-Intervention",
  "RHD-Heart failure", "RHD-Stroke", "RHD-Arrythmia" (sic -- live's own typo,
  reproduced to match, same policy as the earlier BPG "Anamolous" case but
  here it's an answer label living only in this form so I chose to mirror it
  rather than "correct" it, since "RHD-Arrythmia" functions as the actual
  display text users see on both systems), "RHD-Endocarditis" (hyphen, no
  spaces, mixed case) -- was "RHD - intervention" etc. (spaces around hyphen,
  lowercase after). Renamed both the concept FSNs (in
  `rhd_answers_registry.csv` / `rhd_answers_diagnoses.csv`, confirmed unused
  elsewhere in the distro before renaming) and the form JSON answer labels.
- "Outcome" 4th option: live says "Death", distro said "Deceased". Fixed.
- "Details (Hospital Admission)" label: live just says "Details". Fixed the
  form JSON label (left the concept's own fully-specified name as-is since
  that's an internal identifier distinct from the form-rendered label, and
  the conceptset CSV references it by FSN).
- Confirmed field order and count (5 fields, matching live's own "5 fields"
  count in the Clinical Forms list) and the To-Where?/Date-of-Discharge
  conditional-reveal logic already matches live's behavior (verified by
  selecting "Transfer to another facility" on live and observing "To Where?"
  appear).

---

## RHD Allergies

**Same pattern as Interventional Recommendations:** a standalone
`rhd_allergies.json` form existed (wrong encounter type `RHD Consultation
Visit`, Yes/No Penicillin Allergy + conditional Evidence design), while an
embedded repeating `allergyEntry` obsGroup already existed in
`rhd_consultation.json`'s "History and comorbidities" section -- the latter
structurally matches live's actual Allergy/Reaction/Date Added repeating grid
(seen on live patient rhd00005's Consultation Visit), but its *content* (the
"Allergy" select's options and the "Reaction (Allergies)" checklist's options)
did not match live at all.

**Live verified field set** (via live patient rhd00005's Consultation Visit →
Allergy grid → both "Edit" on an existing row and "Add new Allergy" on a
fresh one -- both showed identical fields/options):
- Allergy Name (select, free text allowed): Penicillin (allergy), Penicillin
  (anaphylaxis) -- **only 2 options, no "None"**
- Reaction (select/checklist, free text allowed): Anaphylaxis, Chest
  tightness, Diarrhea, Dizziness or lightheadedness, Fever, Hives (urticaria),
  Mouth or throat swelling, Nausea or vomiting, Rash (itchy or red),
  Shortness of breath, Swelling (angioedema), Wheezing -- **12 options**
- No separate "Other Allergies" text field and no user-facing "Date Added"
  field inside the edit dialog (Date Added is presumably auto-set server-side
  on live; kept in distro as an editable field since EMR data entry often
  needs to backdate, and removing it would only lose capability, not gain
  live-parity, since live's own grid still shows a populated Date Added
  column -- it's just not editable in live's modal, likely defaulted to
  today and editable elsewhere or not at all. Kept as-is rather than guess
  further.).

**Key discovery:** a dormant, already-correct concept existed unused --
`8200713e-8647-52f0-b93d-841e4e9687b7` ("Reaction (Consultation Visit)",
defined in `rhd_questions_consultation.csv`) already had the exact 12-option
answer list matching live (verified by decoding 3 CIEL "AAAA" UUIDs via direct
DB query: 141600=Shortness of breath, 122863=Wheezing, 140238=Fever). This
concept wasn't wired into any form JSON at all before this fix -- reused it
instead of creating new concepts.

**Decisions:**
1. Reworked `rhd_consultation.json`'s embedded `allergyEntry` obsGroup:
   - "Allergy" select: removed the "None" answer (zero obs recorded against
     it, confirmed via DB query), kept Penicillin (allergy)/Penicillin
     (anaphylaxis), renamed "Other non-coded" -> "Other" label. Relabeled the
     field itself from "Allergy" to "Allergy Name" to match live's dialog
     label exactly (the outer obsGroup keeps the "Allergy" label, matching
     live's grid column header).
   - "Reaction (Allergies)" obsGroup/field: repointed to reuse the dormant,
     correct `8200713e-...` concept instead of the wrong 5-option
     `62cb455e-...` concept; relabeled from "Reaction (Allergies)" to
     "Reaction" to match live exactly.
   - Removed the "Other Allergies" free-text field entirely -- live has no
     such field in the Allergy entry dialog, and free text is already
     reachable via the Allergy Name / Reaction "Other" answers.
   - Kept "Date Added" (date field) since live's grid does show this column
     populated, even though it wasn't present as an editable input in the
     dialog I inspected -- removing it would lose capability without clear
     evidence live truly omits user control over it.
2. Retired the standalone `rhd_allergies.json` (same treatment as
   `rhd_recommendations.json` earlier): `retired: true` + explanatory
   description, removing it from the Clinical Forms list.
3. Voided 4 now-orphaned question concepts in `rhd_questions_consultation.csv`
   (Evidence, Other Allergies, Penicillin Allergy, the old 5-option Reaction
   (Allergies)), all confirmed zero `obs` rows via direct DB query before
   voiding.
4. Updated `set_allergies_rhd_section` conceptset's description to note it's
   superseded/historical (left its member list as-is since those member
   concepts are now voided-but-documented, not deleted -- consistent with the
   audit-trail approach used throughout this log).

**Verification:** both `rhd_consultation.json` and `rhd_allergies.json`
validated as parseable JSON; `rhd_questions_consultation.csv` validated as
consistent 11-column CSV. Live UI re-check pending backend restart (batched).

---

## RHD Anticoagulation, RHD Anticoagulation Monitoring, RHD INR Monitoring

**Same orphaned-standalone-form pattern again, x2, plus one real fix:**

1. `rhd_anticoagulation.json` (standalone, wrong encounter type `RHD
   Consultation Visit`, Date + Type-of-Anticoagulation-checkbox with only 5
   drug options) has no live equivalent at all. Live tracks anticoagulants
   (confirmed: a "Warfarin 2.5mg Daily (Q24)" row seen earlier) as part of its
   general Medication list on Consultation Visit, not a dedicated
   anticoagulation form. Our distro already has exactly this: a "Cardiac
   Medication" repeating obsGroup in `rhd_consultation.json`'s "Cardiac
   medications" section with 46 drug options (including Warfarin, Enoxaparin,
   Rivaroxaban, Acitrom) plus Dosing/Dosing Interval/Date Started/Date
   Stopped. **Retired** the standalone form; voided its 2 question concepts
   (zero obs each, confirmed via DB query).
2. `rhd_anticoagulation_monitoring.json` (standalone, single Date field only)
   also has no live equivalent -- live's actual form for this purpose is "RHD
   INR Monitoring" (confirmed live via the "Create form to finalize INR
   Monitoring" link inside Consultation Visit, and the form's own presence
   with real completion timestamps on the test.act.uwdigi.org OpenMRS
   instance). **Retired** the standalone form; voided its 1 question concept
   (zero obs, confirmed via DB query).
3. `rhd_inr_monitoring.json` -- this one IS the correct live-matching form
   (own `RHD INR Monitoring` encounter type, confirmed present and used on
   live), but its field content was significantly off. Opened live's actual
   "INR Monitoring" form fresh (patient rhd00005, via the in-Consultation-Visit
   link) and found:
   - **Indication**: live is a **coded select** with 4 options (Atrial
     Fibrillation, Decreased LV function, Mechanical Valve, Stroke) plus free
     text -- distro had it as a plain free-text field with no options at all.
   - **INR Target**: already matched live exactly (1.5-2.5 / 2.0-3.0 /
     2.5-3.5) -- no change needed.
   - **INR Value repeating entry**: live's dialog has 4 fields -- Date Tested
     (date, required), INR Value (number, required), Dose Change (Yes/No
     select), New Anticoagulant Dose (text, shown only when Dose Change =
     Yes, confirmed by selecting Yes and observing the field appear in the
     DOM). Distro only had 2 of these 4 (Date Updated, INR) -- missing Dose
     Change and the conditional dose-text field entirely.

**Decisions:**
- Recoded "Indication (INR Monitoring)" concept from Text to Coded, reusing
  CIEL "Atrial fibrillation" (148203AAAA...) and the existing custom "Stroke"
  concept, creating 2 new custom concepts (Mechanical Valve, Decreased LV
  function -- no CIEL match found for either) plus an Other/free-text answer.
- Renamed "Date Updated (INR Monitoring)" concept and form label to "Date
  Tested" to match live exactly.
- Relabeled the "INR" field to "INR Value" (kept the same underlying CIEL
  concept 161482AAAA, just a label fix).
- Added two new concepts: "Dose Change" (Coded Yes/No, reusing the project's
  standard Yes/No concept pair) and "New Anticoagulant Dose" (Text, hidden
  unless Dose Change = Yes). Named it "New Anticoagulant Dose" (the grid
  column header text) rather than live's dialog-internal "New Warfarin Dose"
  label, since our Cardiac Medication list already supports many anticoagulants
  beyond Warfarin and the more general label avoids implying the form only
  applies to Warfarin patients -- a deliberate, documented departure from
  live's literal label for semantic correctness.
- Updated `set_anticoagulation_monitoring_consultation_visit_rhd_section`
  conceptset's member list to the new 6-field set and description.
- Updated the two now-superseded conceptsets' descriptions to flag them as
  historical/superseded, left their member lists as-is.

**Verification:** all 3 form JSONs validate as parseable JSON; both touched
CSVs validate as consistent 11-column files. Live UI re-check pending backend
restart (batched).

---

## RHD Cardiac Intervention / RHD Procedures and Outcomes

**Orphaned-duplicate pattern again:** `rhd_cardiac_intervention.json` pointed
at the same `RHD Interventions and Outcomes` encounter type as the real
`rhd_interventions.json` ("Procedures and Outcomes", live's actual 51-field
form), but with its own small, separately-IDed field set (Date of
Intervention, Location, Procedure performed with only 6 options) that
partially overlapped live's real fields (Date of Procedure, Procedure
Location) under different concepts -- risking duplicate/conflicting obs if
both forms were ever used for the same encounter. **Retired** the standalone
form; voided its 3 question concepts (zero obs each, confirmed via DB query);
updated its conceptset's description.

**`rhd_interventions.json` ("Procedures and Outcomes")**: opened live's actual
form (patient rhd00005, an existing completed Surgery-type record) and
cross-checked against the distro JSON. Found `rhd_questions_interventions.csv`
already carries extensive, dated, specific verification notes from an earlier
session ("un-retired 2026-09-16 -- field exists on live... verified live
2026-09-17: hidden under Catheterization", "expanded to full 18/22-option
live answer set", "corrected 2026-09-16 to Coded, multi-select, matching live
exactly", "added 2026-09-16 -- missing field found on live", etc.) -- this
form has clearly already been through a thorough, careful live-comparison
pass. Distro has 56 leaf fields vs live's stated "51 fields" count; the
5-field gap is plausibly conditional/hidden fields live's own count doesn't
surface (matches the pattern of RACHs Score, Mitral Valve Area, etc. being
conditionally shown only for one Procedure Type).

**Decision:** given the scale (56 fields) and the existing careful
documentation, did a **spot-check rather than a full re-audit**: verified
"RACHs Score"'s conditional-visibility note is internally consistent with
what I observed live (a Surgery-type record with no RACHs Score value
recorded -- not a contradiction, since view-mode only renders populated
fields regardless of edit-mode visibility rules). Chose to trust the prior
pass's documented live-verification rather than re-derive all 56 fields from
scratch, to keep the overall multi-form review moving. Flagging this as an
area to revisit with a dedicated, full pass if time allows after the rest of
the forms are done.

---

## RHD Chronic Health Conditions

Same orphaned-standalone pattern: `rhd_chronic_conditions.json` modeled 6
separate category checklists (Hematology/Oncology, Cardiovascular Disorders,
Chronic Respiratory Diseases, Digestive diseases, Neurological/Metal Health,
Diabetes and Kidney Diseases), while live's actual Consultation Visit grid
("Chronic Health Condition" column, confirmed earlier showing "Asthma" and
"Chronic headache" as saved values) uses one simple repeating entry with a
single flattened select. The embedded `chronicHealthConditionEntry` obsGroup
in `rhd_consultation.json` already had exactly this correct shape.

**Live-verified via fresh "Add new Chronic Health Condition" dialog:** 18
options (Anxiety, Asthma, Chronic headache, Chronic kidney disease, Chronic
obstructive pulmonary disease, Cirrhosis / liver disease, Depression, Diabetes
mellitus, Epilepsy, Gastroesophageal reflux, HIV, Hypertension, Ischemic heart
disease, Malignancy / Cancer, Peripheral vascular disease, Sickle Cell
Disease, Stroke, Substance-use disorder) -- matched our existing 18-option
list exactly, with only label-formatting differences: "Cirrhosis / liver
disease" (spaces around slash) vs ours "Cirrhosis/liver disease", "Malignancy
/ Cancer" vs ours "Malignancy/Cancer", "Sickle Cell Disease" (title case) vs
ours "Sickle cell disease". Also: live's dialog field label is "Chronic
Health Condition Name" (not just "Chronic Health Condition", which is the
outer grid/column label), and the field "Allows free text" per live's own UI
copy.

**Decisions:**
1. Renamed 3 concept FSNs (in `rhd_answers_diagnoses.csv`, confirmed reused
   in both `rhd_consultation.json` and the now-retired
   `rhd_chronic_conditions.json` before editing) to match live's exact
   spacing/casing, and updated both forms' JSON labels to match.
2. Renamed the embedded entry's inner select field label from "Chronic Health
   Condition" to "Chronic Health Condition Name" to match live's dialog label
   exactly, keeping the outer obsGroup's label as "Chronic Health Condition"
   to match live's grid column header.
3. Did NOT rename "Other non-coded" to "Other" here (unlike several earlier
   fixes this session) -- live's 18-option dropdown capture did not include a
   distinct "Other" menu entry at all (free text appears to be entered
   directly into the search box without picking an "Other" option first,
   unlike BPG/Oral Adherence fields where an explicit "Other" chip was
   observed). Changing the label without direct evidence would be an
   unfounded guess, so left as-is.
4. Retired the standalone `rhd_chronic_conditions.json`; voided its 6 group
   question concepts (all zero obs, confirmed via DB query); updated its
   conceptset's description.

**Verification:** both form JSONs validate as parseable JSON;
`rhd_questions_consultation.csv` validates as consistent 11-column CSV. Live
UI re-check pending backend restart (batched).

---

## RHD Consent -> RHD Research Participation (full rework, not just a fix)

**Different from every other form so far:** this is not an orphaned duplicate
of an existing correct form -- it's the distro's *only* attempt at this
concern, and it was built around the wrong concept entirely. `rhd_consent.json`
modeled a consent-giver workflow (Study Name select with 2 fabricated study
names, Assent/Consent dates, Name of person consenting, Relation of person
consenting [Mother/Father/Grandmother/etc.], Study Completion date, Reason for
completiong [Final visit/Withdrawn/Lost to follow-up/Deceased]). Live's
corresponding form, called "Research Participation" (confirmed via the
sidebar entry "Research Participation -- Form exists"), has a completely
different shape and purpose: a simple study-enrollment tracker, with **no
consent-giver name or relation fields anywhere**.

**Live verified field set** (patient rhd00005's existing Research
Participation record, both "Would you like to be considered?" top-level field
and an "Edit Participation" dialog on its one existing row "Stage Study 1"):
- Would you like to be considered? (Yes/No)
- Participation (repeating grid: Study, Study ID, Enrollment Date, Completion
  Date, Study Status, Actions)
  - **Study ID** (select, required): ACT Global 2025, ARC, Miller, Stage
    Study 1 -- **4 options, entirely different from distro's 2 fabricated
    study names**
  - Enrollment Date (date)
  - Completion Date (date, optional per live's own "(Optional)" label)
  - **Study Status** (select): Active, Inactive - Death, Inactive - Lost to
    follow-up, Inactive - Study team withdrawal, Inactive - Voluntary
    withdrawal -- **5 options**, replacing the old "Reason for completiong"
    4-option field

**Decision: full rework, not a patch.** Confirmed zero obs recorded against
every one of the old form's 8 question concepts (DB query), so nothing was
lost by replacing rather than layering fixes:
1. Voided the 6 concepts with no live counterpart at all (Assent, Consent,
   Name/Relation of person consenting, Study Completion, old Reason for
   completiong/Study Name), each with a reason pointing at what replaced it.
2. Kept and renamed "Would You Like to Be Considered for Research" ->
   "Would you like to be considered?" (same concept, same semantics, just a
   label match) since it already existed and matches live's top-level field
   exactly.
3. Added 10 new concepts: the "Participation" repeating-group concept, 4
   leaf-field concepts (Study ID, Enrollment Date, Completion Date, Study
   Status), and 8 new answer concepts (ACT Global 2025, ARC, Miller, Stage
   Study 1 for Study ID; Active, Inactive - Death, Inactive - Study team
   withdrawal, Inactive - Voluntary withdrawal for Study Status -- reusing
   the pre-existing "Lost to follow-up" concept for the 5th Study Status
   option instead of minting a duplicate).
4. **Renamed the form itself** from "RHD Consent" to "RHD Research
   Participation" (both the `name` field and the conceptset's FSN/description)
   to match what live actually calls this and avoid the old name misleading
   anyone about what the form now does. Kept the filename `rhd_consent.json`
   unchanged (confirmed via grep it isn't referenced anywhere by exact
   filename, so renaming the file wasn't necessary and would just churn the
   diff).
5. Kept `encounter: "RHD Consultation Visit"` as-is since this is still a
   Consultation-Visit-adjacent concern on live (the Research Participation
   form sits alongside Consultation Visit in the per-patient forms list, not
   under its own dedicated encounter type) -- did not invent a new encounter
   type for this.

**Verification:** `rhd_consent.json` validates as parseable JSON;
`rhd_questions_consent.csv` validates as consistent 11-column CSV. Live UI
re-check pending backend restart (batched).

---

## RHD Consultation Update

**Different from the other orphaned forms: no live equivalent exists at
all.** `rhd_consultation_update.json` ("Follow-up Appointments" checkbox
[RHD clinic/Tertiary center] + conditional "Reason for RHD Clinic
Appointment" select) pointed at the `RHD Consultation Visit` encounter type,
same pattern as several forms fixed earlier this session -- but unlike
Interventional Recommendations, Allergies, or Chronic Health Conditions,
there is no corresponding embedded section in `rhd_consultation.json` for
this concern, and direct inspection of live's full Consultation Visit form in
edit mode (all 65 fields rendered, checked via `document.body.textContent`
for "Follow-up Appointment", "RHD clinic", "Tertiary center") found **zero
matches anywhere**. This concern was fabricated during the original
migration with nothing on live to point to.

**Decision:** retired the form outright (same `retired: true` + explanatory
description treatment as other retirements this session); voided its 2
question concepts (zero obs each, confirmed via DB query); updated its
conceptset's description. No replacement field was added anywhere, since
there is nothing on live to replace it with.

**Verification:** form JSON validates as parseable; touched CSV validates as
consistent 11-column file.

---

## RHD Echocardiogram / RHD Electrocardiogram

Both already correctly use their own dedicated encounter types (matching
live's standalone forms, confirmed present with the branch name
`fix/echo-mitral-repair-and-ecg-multiselect` signaling prior dedicated work
on exactly these two). Did a live spot-check rather than a full re-derivation
given the apparent prior care.

**Echocardiogram:** live-verified field list and all 4-tier (None/Mild/
Moderate/Severe), LV Function (Normal/Mildly Decreased/Moderately Decreased/
Severely Decreased) and LV Size (Normal/Mildly Dilated/Moderately Dilated/
Severely Dilated) answer sets. Found and fixed 3 small label mismatches:
- "Mitral stenosis severity" -> "Mitral Stenosis" (both form JSON label and
  concept FSN, confirmed not reused elsewhere before renaming)
- "Pulmonary hypertension severity" -> "Pulmonary Hypertension" (same)
- "Mildly decreased"/"Moderately decreased"/"Severely decreased" ->
  "Mildly Decreased"/"Moderately Decreased"/"Severely Decreased"
  (capitalization only, form JSON label)

**Electrocardiogram:** live-verified the full 24-option "Electrocardiogram
Result" checklist (confirmed checkbox/multiselect rendering already correct,
matching the branch name's "ecg-multiselect" prior fix) -- 24/24 options
present and in the same order, with 4 label mismatches:
- "Normal sinus rhythm" -> "Sinus rhythm" (form JSON label only -- kept the
  underlying concept's own FSN "Normal sinus rhythm" / short name "NSR"
  unchanged, since that's a more complete clinical term and the concept
  itself isn't wrong, just the form's display label needed to match live)
- "Third degree heart block" -> "Third degree (complete) heart block" (form
  JSON label only -- the concept's own FSN already said "Third degree
  (complete) heart block", so this was purely a stale form-label fix)
- "Right bundle branch block" -> "RBBB" (form JSON label only -- concept
  already has short name "RBBB")
- "Left bundle branch block" -> "LBBB" (form JSON label only -- concept
  already has short name "LBBB")

**Verification:** both form JSONs validate as parseable; touched CSV
validates as consistent 11-column file.

---

## RHD Patient Information

Unlike most forms reviewed this session, live has a real standalone "Patient
Information" form (confirmed, "Up to 22 fields" in the sidebar, pointing at
`rhd00005`'s own patient-information route rather than a visit/encounter
route) -- but it is a much richer core-demographics form than our distro's
clinical-forms-only subset (it includes First/Family Name, Alternate ID, DOB,
Age, Sex, Cardiac/Primary Care Clinic assignment -- all core-OpenMRS-adjacent
fields our distro handles via patient registration, not this form). Focused
the comparison on the fields that are genuinely this form's clinical-data
responsibility, matching our distro's existing scope.

**Live-verified, 2 real fixes:**

1. **Category at Diagnosis**: live's "Diagnosis Category" field (inside the
   repeating Diagnosis grid's edit dialog) has only 4 broad options
   (Rheumatic Heart Disease/Rheumatic Fever; Congenital Heart Disease; Other
   Heart Disease; Normal). Distro had 8 options including granular RHD A-D
   staging, "Acute rheumatic fever", and "Screen + pending confirmatory
   echo" -- none of which exist as Diagnosis Category options on live; that
   level of detail belongs in the free-text Diagnosis Details field instead
   (confirmed: a real live patient's Diagnosis Category was "Rheumatic Heart
   Disease/Rheumatic Fever" while Diagnosis Details literally said "RHD A").
   Recoded to the 4-option live set, voided the 6 no-longer-used answer
   concepts (zero obs each, confirmed via DB query) and 1 reused-but-renamed
   concept (fixed casing: "Rheumatic heart Disease/Rheumatic fever" ->
   "Rheumatic Heart Disease/Rheumatic Fever").
2. **Death section replaced entirely.** Distro modeled a conditional
   Date-of-Death/Details/Pregnant-or-Postpartum block hidden behind the core
   OpenMRS `patient.deceasedBoolean` flag. Live instead drives this entirely
   through form obs: a top-level **Status** field (Active/Inactive) reveals
   **Reason Inactive** (6 options: CHD spontaneous resolution, Confirmatory
   echo normal, Death, Lost to follow-up, Physical relocation, Voluntary
   inactivity) when Inactive; Reason Inactive = Physical relocation reveals
   **Relocation Area** (already existed in the distro, moved under this
   conditional instead of always-visible); Reason Inactive = Death reveals
   **Cause of Death** (11 options), **Date of Death**, and **Details of
   Death**. Rebuilt the whole section to match, reusing existing concepts
   wherever possible (CHD spontaneous resolution, Confirmatory echo normal,
   Lost to follow-up, Physical relocation, Voluntary inactivity, Adverse drug
   reaction, Anticoagulation related hemorrhage, Catheterization, Stroke,
   Trauma, Within 30-days of cardiac surgery were all already present and
   unused elsewhere) and reusing RHD-Heart failure/RHD-Arrythmia (minted
   earlier this session for Hospital Admission) with form-level label
   overrides "Heart failure"/"Arrhythmia" for Cause of Death, since live uses
   the shorter label here. Voided the fabricated "Pregnant or Postpartum (60
   Days After Delivery) at Time of Death" field (zero obs, confirmed via DB
   query; also confirmed absent from live via full-page-text search with
   Status=Inactive/Reason Inactive=Death expanded). Renamed "Details of
   events leading up to death" -> "Details of Death" to match live exactly
   (kept the same underlying concept rather than minting a duplicate).
3. Fixed a duplicate-field-id bug this rework would otherwise have
   introduced: the old unconditional "Relocation Area" field in "Contact and
   location" and the new conditional one under Status shared the same
   concept and `id` -- removed the old unconditional copy, keeping only the
   new conditionally-shown one (matching live's actual behavior, where
   Relocation Area only appears for the Physical-relocation reason).
4. Fixed "Pregnancy/labor" -> "Pregnancy / labor" label in Case Detected By
   to match live's exact spacing (same fix pattern as several earlier forms).

**Known gap, flagged rather than fixed:** live's Diagnosis is actually a
repeating grid (Category, Details, Priority [Primary/Secondary], Date
Updated, Date Ended, Active) -- the same "flat fields vs. repeating grid"
pattern already corrected for Interventional Recommendations, Allergies, and
Chronic Conditions earlier this session. Given the time already spent on
this one form and that RHD patients almost always carry a single primary
diagnosis (the flat-fields case the distro already handles), chose not to
rebuild this as a full repeating grid right now. **Revisit this as a
dedicated follow-up** if a patient ever needs multiple concurrent/historical
diagnoses tracked with priority and active/inactive status.

**Verification:** form JSON validates as parseable with no duplicate field
ids (16 total leaf fields); all touched CSVs validate as consistent
11-column files.

---

## RHD Pregnancy (full rework)

Already correctly uses its own dedicated `RHD Pregnancy` encounter type
(confirmed live-present: "Pregnancy 19 fields" in the sidebar for a female
test patient, rhd00009). Went field-by-field against a real live Pregnancy
record in edit mode, enumerating every `<label>`-to-`<input name>` pairing
directly via the DOM, which gave an authoritative field list and order.

**Live-verified: 22 fields, one flat list (no section split)** -- confirmed
no section sub-headings exist inside the live form via a heading-element
query, so the distro's original 2-section split ("Pregnancy details" /
"Delivery and outcomes") was flattened into one section to match.

**Fixes applied:**
1. **4 missing fields added**: Date of Service (first field on live, missing
   entirely from distro), Date of Delivery (missing, sits right after Number
   of Fetuses), Describe Maternal Cause of Death Further, Describe Neonatal
   Cause of Death Further (both missing, each immediately follows its
   Cause-of-Death select on live).
2. **1 fabricated field voided**: Pregnancy Number -- no equivalent on live's
   full field list (confirmed via the live input-name enumeration); zero obs
   recorded, confirmed via DB query. Gravidity/Parity already cover this.
3. **Fetal Outcome: multiselect -> single-select.** Distro rendered this as
   `checkbox` (multiselect) wrapped in an obsGroup; live renders it as a
   single-select (confirmed by checking for react-select's `multiValue` CSS
   class, which is absent). Fetal outcomes are mutually exclusive on live
   (a pregnancy has one fetal outcome, not several), so this was a real
   correctness bug, not just a label mismatch. Un-nested from its obsGroup
   wrapper since a single-select field doesn't need one.
4. **5 fields recoded from free Text to Coded**, each with live's exact
   answer set, now that the answer sets are resolved (the CSV previously
   carried "coded in spec but no answer set resolved -- using Text" notes
   for two of these):
   - Number of Fetuses: 1, 2, 3, Other (3 new simple concepts minted, no CIEL
     match for bare numerals in this context)
   - Maternal Complications During Pregnancy: 9 options, 7 already existed
     unused in `rhd_answers_pregnancy.csv` (Death, Eclampsia via CIEL,
     Hospitalization, New onset heart failure, Placental Abruption via CIEL,
     Pre-eclampsia via CIEL, Rhythm disturbance) plus Stroke and None
   - Maternal Complications Postpartum: same 9-option set minus Stroke (8
     options) -- live genuinely excludes Stroke as a postpartum-complications
     option even though it's a pregnancy-complications option, confirmed by
     directly comparing the two live dropdowns' option arrays
   - Maternal Cause of Death: 5 options -- Heart Failure (reused
     RHD-Heart failure concept with label override, same reasoning as
     Patient Information's Cause of Death field above), Hemorrhage (CIEL),
     Sepsis (CIEL), Stroke, Other Infectious Disease (1 new concept minted)
   - Neonatal Cause of Death: 4 options -- Congenital Malformation (CIEL),
     Infection (1 new concept, no CIEL match), Prematurity (1 new concept, no
     CIEL match), Unknown (CIEL)
5. **8 label-only fixes** to match live exactly: "Delivery mode" ->
   "Delivery Mode"; "Location of Delivery" -> "Delivery Location"; "Other
   non-coded" -> "Other" (Delivery Location); "Maternal Complications Within
   30 Days After Pregnancy" -> "Maternal Complications Postpartum"; "Neonatal
   weight (kg)" -> "Birth Weight"; "Neontal Cause of Death" -> "Neonatal
   Cause of Death" (fixed the typo); "Neonatal Outcomes 30 Days After
   Delivery" -> "Neonatal Outcomes"; "Fetal Outcomes" -> "Fetal Outcome"
   (singular, matching live and the multiselect->single-select fix above).
6. **Delivery Mode answer order flipped** to match live exactly (Cesarean
   section listed before Vaginal birth on live; was reversed in distro) --
   cosmetic only, doesn't affect correctness, but matched anyway for
   consistency with the rest of this review's "match live exactly" standard.
7. Reused "Estimated Gestational Age at Delivery (weeks)" concept for live's
   "Gestational Age (in weeks)" label via a form-level label override, rather
   than minting a near-duplicate concept.

**Verification:** form JSON validates as parseable with no duplicate field
ids (22 total leaf fields, matching live's exact count); touched CSVs
validate as consistent 11-column files.

---

## Progress checklist (all forms in distro/configuration/ampathforms/)

- [x] rhd_consultation (Urgency fix, embedded Interventional Recommendations)
- [x] rhd_recommendations (retired, superseded)
- [x] rhd_adherence (Oral Adherence) -- see below
- [x] rhd_hospital (Hospital Admission) -- revisited, see below
- [x] rhd_allergies -- retired, superseded (see below)
- [x] rhd_anticoagulation -- retired, superseded (see below)
- [x] rhd_anticoagulation_monitoring -- retired, superseded (see below)
- [x] rhd_inr_monitoring -- reworked to match live (see below)
- [x] rhd_bpg (already fully verified in a prior session -- no regression found, skipped)
- [x] rhd_cardiac_intervention -- retired, superseded (see below)
- [x] rhd_interventions (Procedures and Outcomes) -- spot-checked, trusted prior work (see below)
- [x] rhd_chronic_conditions -- retired, superseded (see below)
- [x] rhd_consent -- reworked entirely, renamed to RHD Research Participation (see below)
- [x] rhd_consultation_update -- retired, fabricated (see below)
- [x] rhd_echocardiogram -- small label fixes (see below)
- [x] rhd_electrocardiogram -- small label fixes (see below)
- [x] rhd_patient_information -- partial rework (see below), Diagnosis-grid gap flagged for later
- [x] rhd_pregnancy -- full rework (see below)

---

## RHD Oral Adherence

**Live verified field set** (via live patient rhd00005, fresh/blank "Enter Oral
Prophylaxis" form, all 5 fields read directly from the DOM/react-select props):

1. Date of Adherence Estimate (date, **required**)
2. Adherence Estimate (%) (number)
3. Adherence Based On (select, required, free text allowed): Calendar, Patient
   report, Pill count -- **3 options, no "Other"**
4. Explanation if Adherence is Below 80% (select, free text allowed, hidden
   unless adherence < 80%): Could not afford medication, Forgot, Medication
   has side effects, Medication ran out -- **4 options**
5. New Prescription Duration (select): 1 month, 3 months, 6 months, 12 months

**Distro before fix:** 8 fields, diverged heavily:
- Missing the required Date of Adherence Estimate field entirely.
- "Adherence Estimate Based On" had a bogus "Other non-coded" answer not on live.
- "Explanation if Adherence is Below 80%" was a free-text field instead of a
  coded select with live's 4 reasons.
- "Prescription Duration" label didn't match live's "New Prescription Duration".
- 4 fabricated fields with zero live counterpart and zero recorded obs data:
  Weeks in Reporting Period, Other Details (Oral Adherence), Dosing (Oral
  Adherence), Tablets Given/Prescribed.

**Decisions:**
1. Added a new "Date of Adherence Estimate" Date-type question concept
   (deterministic UUID via the project's `question:` slug convention) and
   marked it `required: true` in the form, matching live.
2. Removed the "Other non-coded" answer from Adherence Based On; marked the
   field `required: true` to match live.
3. Recoded "Explanation if Adherence is Below 80%" from Text to Coded, reusing
   4 pre-existing concepts from `rhd_answers_registry.csv` (Could not afford
   medication / Forgot / Medication has side effects / Medication ran out --
   already present, just not previously wired to this question) plus a 5th
   "Other" (CIEL 5622AAAA...) to preserve free-text capability, matching the
   "* Allows free text" live UI affordance.
4. Renamed "Prescription Duration" -> "New Prescription Duration" to match
   live's exact label.
5. Renamed "Adherence Estimage" -> "Adherence Estimate (%)" (fixing a pre-existing
   typo/label mismatch) to match live exactly.
6. **Voided** (did not delete) the 4 fabricated fields' concepts in
   `rhd_questions_adherence.csv` with a clear reason, confirmed zero `obs` rows
   reference any of them first via a direct DB query, and removed them from
   both the form JSON and the `set_oral_adherence_rhd_section` conceptset.
   Chose void-with-reason over silent deletion to preserve an audit trail,
   consistent with how earlier BPG fabricated-field cleanup was handled this
   project.

**Verification:** `rhd_adherence.json` validated as parseable JSON;
`rhd_questions_adherence.csv` validated as consistent 11-column CSV across all
rows. Live UI re-check pending backend restart (batched with later forms to
avoid restarting after every single form).

---
