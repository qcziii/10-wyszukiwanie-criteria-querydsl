#!/usr/bin/env bash
# Check for scenario 13-projekcja-bean (the description is printed below)
# Run: ./scenarios/13-projekcja-bean/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 13-projekcja-bean: Na liście podpowiedzi names produktów to names kategorii"
run_tests "$SCEN_DIR"
summary
