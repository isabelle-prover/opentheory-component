#!/bin/bash

set -e

echo "Checking Isabelle ..."
if [ -x "$ISABELLE_TOOL" ]; then
  echo "Isabelle present at $ISABELLE_TOOL"
else
  echo "Isabelle not present. Run this using 'isabelle env ./install.sh', or set the environment variable \$ISABELLE_TOOL."
  exit 1
fi

echo "Checking if already installed ..."
ISABELLE_COMPONENTS="$("$ISABELLE_TOOL" getenv -b ISABELLE_COMPONENTS)"
IFS=: read -r -d '' -a components < <(printf '%s:\0' "$ISABELLE_COMPONENTS")
CUR_DIR="$(pwd)"

for component in "${components[@]}"; do
  if [ "$component" = "$CUR_DIR" ]; then
    echo "Already installed, exiting"
    exit 0
  fi
done

DIR="$("$ISABELLE_TOOL" getenv -b ISABELLE_HOME_USER)"

echo "Installing into '$DIR/etc/components' ..."

mkdir -p "$DIR/etc"
echo "$CUR_DIR" >> "$DIR/etc/components"

echo "Done."
