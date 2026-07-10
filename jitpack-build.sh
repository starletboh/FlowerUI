#!/bin/bash

TAG=$(git describe --tags --exact-match 2>/dev/null)

echo "Building tag: $TAG"

if [[ "$TAG" == *"-mc"* ]]; then
    MC_VERSION=${TAG#*-mc}

    echo "Detected Minecraft version: $MC_VERSION"

    ./gradlew :versions:$MC_VERSION:publishToMavenLocal
else
    echo "Building common library"

    ./gradlew :common:publishToMavenLocal
fi