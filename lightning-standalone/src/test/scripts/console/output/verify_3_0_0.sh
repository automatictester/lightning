#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/3_0_0.txt"
ACTUAL_RESULT="src/test/resources/results/actual/3_0_0.txt"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/3_0_0.xml \
    --jmeter-csv src/test/resources/csv/jmeter/10_transactions.csv \
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