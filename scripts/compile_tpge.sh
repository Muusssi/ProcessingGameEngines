#!/bin/bash -ex

echo "-- Compiling TPGE package --"

# Create a temporary library for the class files
mkdir -p tmp

# Compile the jave code
javac $(find src/tge -name '*.java') \
      $(find src/tpge -name '*.java') \
      -d tmp/ \
      -classpath core.jar

# Ensure the output folder exists
mkdir -p jars

# Collect to jar
cd tmp
jar cf ../jars/tpge.jar ./
cd ..

# Remove temp file
rm -r tmp
