#!/usr/bin/env bash
# Check for scenario 05-zakres-dat (the description is printed below)
# Run: ./scenarios/05-zakres-dat/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 05-zakres-dat: Raport dodane do 31.03 nie contains produktów z 31.03"
run_tests "$SCEN_DIR"
summary
