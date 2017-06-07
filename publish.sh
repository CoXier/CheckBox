#!/bin/sh
./gradlew clean build bintrayUpload -PdryRun=false
