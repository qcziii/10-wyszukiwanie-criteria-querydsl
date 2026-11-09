#!/usr/bin/env bash
# Check for scenario 11-znikaja-produkty-bez-kategorii (the description is printed below)
# Run: ./scenarios/11-znikaja-produkty-bez-kategorii/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 11-znikaja-produkty-bez-kategorii: Produkty bez kategorii znikają z wyników QueryDSL"
run_tests "$SCEN_DIR"
summary
