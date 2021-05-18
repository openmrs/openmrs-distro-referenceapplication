#!/usr/bin/env bash
set -e
npx openmrs@$1 assemble --mode config --target $2 --config $3
