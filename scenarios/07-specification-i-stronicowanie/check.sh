#!/usr/bin/env bash
# Check for scenario 07-specification-i-stronicowanie (the description is printed below)
# Run: ./scenarios/07-specification-i-stronicowanie/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 07-specification-i-stronicowanie: Po przejściu na Specification paging kończy się błędem"
run_tests "$SCEN_DIR"
summary
