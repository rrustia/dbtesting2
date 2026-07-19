#!/bin/sh
# Maven Wrapper script - download Maven if not present locally
# This lets you build the project without having Maven installed on your system.
# Just run: ./mvnw clean package

WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    mkdir -p .mvn/wrapper
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" || wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
fi

exec java -jar "$WRAPPER_JAR" "$@"
