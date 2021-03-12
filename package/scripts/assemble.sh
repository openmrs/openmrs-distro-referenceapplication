#!/usr/bin/env bash
set -e
npx openmrs assemble --mode config --target $1 --config $2
