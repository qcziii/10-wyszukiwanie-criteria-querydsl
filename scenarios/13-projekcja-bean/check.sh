#!/usr/bin/env bash
# Scenariusz 13-projekcja-bean: Na liście podpowiedzi nazwy produktów to nazwy kategorii
# Uruchom: ./scenarios/13-projekcja-bean/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 13-projekcja-bean: Na liście podpowiedzi nazwy produktów to nazwy kategorii"
run_tests "$SCEN_DIR"
summary
