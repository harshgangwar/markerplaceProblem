#!/usr/bin/env bash
set -e
rm -rf out
mkdir -p out
find src/main/java -name '*.java' > sources.txt
javac -source 1.8 -target 1.8 -d out @sources.txt
