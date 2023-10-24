#!/bin/bash

file_path="frontend/spa-build-config.json"

repositories=(
  "patient_management"
  "patient_chart"
  "esm_core"
  "form_builder"
  "cohort_builder")

apps=(
  "@openmrs/esm-patient-registration-app"
  "@openmrs/esm-patient-chart-app"
  "@openmrs/esm-login-app"
  "@openmrs/esm-form-builder-app"
  "@openmrs/esm-cohort-builder-app")

for ((i=0; i<${#repositories[@]}; i++)); do
  dep="${repositories[$i]}"
  key="${apps[$i]}"
  value=$(grep "\"$key\":" "$file_path" | awk -F'"' '{print $4}')
  echo "$dep=$value" >> "$GITHUB_OUTPUT"
done
