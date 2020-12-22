#!/bin/bash

mkdir -p src/test/resources/results/actual/junit/
rm -f junit.xml

EXPECTED_RESULT="src/test/resources/results/expected/junit/junit_expected.xml"
ACTUAL_RESULT="junit.xml"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/junit_report.xml \
    --jmeter-csv src/test/resources/csv/jmeter/2_transactions.csv \
    --perfmon-csv src/test/resources/csv/perfmon/junit_report.csv \
    &> /dev/null

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