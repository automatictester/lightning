#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/regexp.txt"
ACTUAL_RESULT="src/test/resources/results/actual/regexp.txt"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/regexp.xml \
    --jmeter-csv src/test/resources/csv/regexp.csv \
    | grep -v "Execution time:" > $ACTUAL_RESULT

DIFF_OUTPUT=`diff $EXPECTED_RESULT $ACTUAL_RESULT`
OUT=$?

echo -e ''; echo `basename "$0"`

if [ $OUT -eq 0 ];then
    echo "OUTPUT AS EXPECTED"
    echo "TEST PASSED"
    exit 0
else
    echo "INCORRECT CONSOLE OUTPUT - DIFF:"
    echo $DIFF_OUTPUT
    echo "TEST FAILED"
    exit 1
fi