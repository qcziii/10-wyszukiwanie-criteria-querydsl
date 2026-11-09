#!/usr/bin/env bash
# Check for scenario 02-kolejny-where (the description is printed below)
# Run: ./scenarios/02-kolejny-where/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 02-kolejny-where: Z dwoma filtrami działa tylko jeden"
run_tests "$SCEN_DIR"
summary
