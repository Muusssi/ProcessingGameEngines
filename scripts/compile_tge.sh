#!/bin/bash -ex

# Create a temporary library for the class files
mkdir -p tmp
# Compile the jave code
javac $(find src/tge -name '**.java') \
      -d tmp/ \
      -cp core.jar

# Ensure the output folder exists
mkdir -p jars

# Collect to jar
cd tmp
jar cf ../jars/tge.jar ./
cd ..

# Remove temp file
rm -r tmp
