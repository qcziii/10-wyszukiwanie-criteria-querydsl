#!/usr/bin/env bash
# Check for scenario 15-n-plus-1 (the description is printed below)
# Run: ./scenarios/15-n-plus-1/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 15-n-plus-1: Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań"
run_tests "$SCEN_DIR"
summary
