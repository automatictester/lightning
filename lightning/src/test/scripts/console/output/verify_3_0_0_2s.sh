#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/1_client_2_server.txt"
ACTUAL_RESULT="src/test/resources/results/actual/1_client_2_server.txt"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/1_client_2_server.xml \
    --jmeter-csv src/test/resources/csv/jmeter/10_transactions.csv \
    --perfmon-csv src/test/resources/csv/perfmon/2_entries.csv \
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