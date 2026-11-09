#!/usr/bin/env bash
# Check for scenario 10-niemutowalne-wyrazenie (the description is printed below)
# Run: ./scenarios/10-niemutowalne-wyrazenie/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 10-niemutowalne-wyrazenie: Filtr ceny w wersji QueryDSL jest ignorowany"
run_tests "$SCEN_DIR"
summary
