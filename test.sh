
#!/bin/bash

# Stop execution immediately if any command fails
set -e

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_ROOT"

MAIN_OUT="out/production/marketplaceProblem"
TEST_OUT="out/test/marketplaceProblem"

echo "======================================"
echo " Compiling Marketplace Production Code"
echo "======================================"

mkdir -p "$MAIN_OUT"

javac -source 8 -target 8 \
  -d "$MAIN_OUT" \
  $(find src/main/java -name "*.java")

echo ""
echo "======================================"
echo " Compiling Marketplace Test Code"
echo "======================================"

mkdir -p "$TEST_OUT"

javac -source 8 -target 8 \
  -cp "$MAIN_OUT" \
  -d "$TEST_OUT" \
  $(find src/test/java -name "*.java")

echo ""
echo "======================================"
echo " Running Marketplace Tests"
echo "======================================"

java -cp "$TEST_OUT:$MAIN_OUT" \
  com.marketplace.TestRunner

echo ""
echo "======================================"
echo " All tests executed successfully!"
echo "======================================"
