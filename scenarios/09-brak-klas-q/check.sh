#!/usr/bin/env bash
# Check for scenario 09-brak-klas-q (the description is printed below)
# Run: ./scenarios/09-brak-klas-q/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 09-brak-klas-q: Po dodaniu metamodelu Hibernate kompilacja nie widzi QProduct"
run_tests "$SCEN_DIR"
summary
