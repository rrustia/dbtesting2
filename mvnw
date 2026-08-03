#!/bin/sh

if [ -n "$MAVEN_HOME" ] && [ -x "$MAVEN_HOME/bin/mvn" ]; then
  exec "$MAVEN_HOME/bin/mvn" "$@"
fi

if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi

echo "Maven was not found. Install Maven 3.9+ or set MAVEN_HOME." >&2
exit 1
