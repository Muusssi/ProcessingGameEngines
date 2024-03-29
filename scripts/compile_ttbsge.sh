#!/bin/bash -ex

# Create a temporary library for the class files
mkdir -p tmp

# Compile the jave code
javac $(find src/tge -name '*.java') \
      $(find src/ttbsge -name '*.java') \
      -d tmp/ \
      -classpath core.jar

# Ensure the output folder exists
mkdir -p jars

# Collect to jar
cd tmp
jar cf ../jars/ttbsge.jar ./
cd ..

# Remove temp file
rm -r tmp
