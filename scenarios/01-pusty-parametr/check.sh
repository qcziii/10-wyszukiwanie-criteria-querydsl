#!/usr/bin/env bash
# Check for scenario 01-pusty-parametr (the description is printed below)
# Run: ./scenarios/01-pusty-parametr/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 01-pusty-parametr: Z pustym polem category wyszukiwarka nic nie zwraca"
run_tests "$SCEN_DIR"
summary
