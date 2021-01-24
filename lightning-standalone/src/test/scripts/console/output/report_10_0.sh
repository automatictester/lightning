#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/report.txt"
ACTUAL_RESULT="src/test/resources/results/actual/report.txt"
PROCESSED_ACTUAL_RESULT="src/test/resources/results/actual/processed_report.txt"

java \
    -jar target/lightning*.jar \
    report \
    --jmeter-csv src/test/resources/csv/jmeter/10_transactions.csv \
    &> $ACTUAL_RESULT

sed -e "s/\[main\] INFO //g" $ACTUAL_RESULT > $PROCESSED_ACTUAL_RESULT

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