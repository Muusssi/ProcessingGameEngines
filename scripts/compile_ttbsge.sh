#!/bin/bash -ex

# Create a temporary library for the class files
mkdir -p tmp
# Compile the jave code
javac $(find src/tge -name '*.java') \
      $(find src/ttbsge -name '*.java') \
      -d tmp/ \
      -classpath core.jar
# Collect to jar
cd tmp
jar cf ../ttbsge.jar ./
cd ..
# Remove temp file
rm -r tmp

# # Copy source code to the library
# mkdir -p library/TTDGE/src
# cp src/ttdge/*.java library/TTDGE/src/
# # Archive the library
# rm library/TTDGE.zip
# zip -r -q --exclude=*.DS_Store* library/TTDGE.zip library/TTDGE

