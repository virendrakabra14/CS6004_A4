#!/bin/bash

# Usage: bash run.sh <cp-directory> <classes-to-analyse-with-main-class-at-first-position>
# e.g., bash run.sh . Test4 Node4

# clean
rm -f *.class
if [ -d "sootOutput" ]; then
    rm -f sootOutput/*.class
fi

# assuming aliases set as openj9-javac, openj9-java
shopt -s expand_aliases
source ~/.bash_aliases

# compile
openj9-javac -cp '.:sootclasses-trunk-jar-with-dependencies.jar' *.java 2>/dev/null || exit 1

# run transformation
openj9-java -cp '.:sootclasses-trunk-jar-with-dependencies.jar' PA4 "$@" 2>/dev/null 1>&2 || exit 1

# create regex for benchmark
# e.g. "CS6004.*(Test|Node)\."
classes=("${@:2}")
regex="CS6004.*("
for class in "${classes[@]}"; do
    regex+="$class|"
done
regex="${regex%|}"
regex+=")\."

# run test
openj9-java -cp '.:sootclasses-trunk-jar-with-dependencies.jar' -Xint "$2" 2>&1 | grep -E "$regex" | wc -l
cd sootOutput
openj9-java -cp '.:sootclasses-trunk-jar-with-dependencies.jar' -Xint "$2" 2>&1 | grep -E "$regex" | wc -l
cd ..
