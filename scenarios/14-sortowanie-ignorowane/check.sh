#!/usr/bin/env bash
# Check for scenario 14-sortowanie-ignorowane (the description is printed below)
# Run: ./scenarios/14-sortowanie-ignorowane/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 14-sortowanie-ignorowane: Parametr sort w wersji QueryDSL nic nie zmienia"
run_tests "$SCEN_DIR"
summary
