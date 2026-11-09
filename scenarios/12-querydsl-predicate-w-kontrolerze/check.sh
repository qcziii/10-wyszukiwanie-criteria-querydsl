#!/usr/bin/env bash
# Check for scenario 12-querydsl-predicate-w-kontrolerze (the description is printed below)
# Run: ./scenarios/12-querydsl-predicate-w-kontrolerze/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 12-querydsl-predicate-w-kontrolerze: Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu"
run_tests "$SCEN_DIR"
summary
