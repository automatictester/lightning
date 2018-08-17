#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/3_0_0.txt"
ACTUAL_RESULT="src/test/resources/results/actual/3_0_0.txt"
PROCESSED_ACTUAL_RESULT="src/test/resources/results/actual/processed_3_0_0.txt"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/3_0_0.xml \
    --jmeter-csv src/test/resources/csv/jmeter/10_transactions.csv \
    &> $ACTUAL_RESULT

cat $ACTUAL_RESULT | grep -v "Execution time:" | \
    sed -e "s/\[main\] INFO //g" &> $PROCESSED_ACTUAL_RESULT

DIFF_OUTPUT=`diff -B $EXPECTED_RESULT $PROCESSED_ACTUAL_RESULT`
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