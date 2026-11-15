#!/usr/bin/env bash
set -e
if [ ! -d out ]; then
  ./build.sh
fi
java -cp out com.marketplace.app.Main
