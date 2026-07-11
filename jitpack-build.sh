#!/bin/bash

# Use JitPack's own $VERSION env var (set to exactly the version string that
# was requested, e.g. "1.0.0-mc26.1") instead of `git describe --tags`.
# git describe is unreliable here: if two tags point at the same commit
# (e.g. "1.0.0" and "1.0.0-mc26.1"), it can resolve to the wrong one and
# silently build/publish the wrong module under the requested version's name.
TAG="$VERSION"

if [[ -z "$TAG" ]]; then
    # Fallback for local/manual runs where $VERSION isn't set by JitPack.
    TAG=$(git describe --tags --exact-match 2>/dev/null)
fi

echo "Building version: $TAG"

if [[ "$TAG" == *"-mc"* ]]; then
    MC_VERSION=${TAG#*-mc}

    echo "Detected Minecraft version: $MC_VERSION"

    ./gradlew ":versions:$MC_VERSION:publishToMavenLocal"
else
    echo "Building common library"

    ./gradlew :common:publishToMavenLocal
fi