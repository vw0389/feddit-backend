#!/usr/bin/env bash
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd ${SCRIPT_DIR}/../
./mvnw dependency:sources
./mvnw dependency:resolve -Dclassifier=javadoc

echo "acquired javadoc and sources"