#!/bin/bash -ex

echo "-- Compiling TRPGE package --"

# Create a temporary library for the class files
mkdir -p tmp

# Compile the jave code
javac $(find src/tge -name '*.java') \
      $(find src/trpge -name '*.java') \
      -d tmp/ \
      -classpath core.jar

# Ensure the output folder exists
mkdir -p jars

# Collect to jar
cd tmp
jar cf ../jars/trpge.jar ./
cd ..

# Remove temp file
rm -r tmp
