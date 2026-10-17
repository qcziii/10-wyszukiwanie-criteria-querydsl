#!/usr/bin/env bash
# Scenariusz 12-querydsl-predicate-w-kontrolerze: Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu
# Uruchom: ./scenarios/12-querydsl-predicate-w-kontrolerze/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 12-querydsl-predicate-w-kontrolerze: Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu"
run_tests "$SCEN_DIR"
summary
