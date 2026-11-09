#!/usr/bin/env bash
# Check for scenario 06-powtorzone-produkty (the description is printed below)
# Run: ./scenarios/06-powtorzone-produkty/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 06-powtorzone-produkty: Po filtrze po ocenie products się powtarzają"
run_tests "$SCEN_DIR"
summary
