#!/usr/bin/env bash
# Runs check.sh of all scenarios and prints a summary.
ROOT=$(cd "$(dirname "$0")/.." && pwd)
ok=0; fail=0; failed=()
for s in "$ROOT"/scenarios/*/; do
  name=$(basename "$s")
  if bash "$s/check.sh" > "$s/target-check.log" 2>&1; then
    echo "[OK]   $name"; ok=$((ok+1))
  else
    echo "[FAIL] $name"; fail=$((fail+1)); failed+=("$name")
  fi
  rm -f "$s/target-check.log"
done
echo
echo "Scenariusze: $ok OK, $fail FAIL"
