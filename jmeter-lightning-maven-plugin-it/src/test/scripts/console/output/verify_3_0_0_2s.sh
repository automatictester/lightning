#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/1_client_2_server.txt"
ACTUAL_RESULT="src/test/resources/results/actual/1_client_2_server.txt"
ACTUAL_RESULT_PARSED="src/test/resources/results/actual/1_client_2_server_parsed.txt"

mvn clean verify \
    -Dmode=verify \
    -DtestSetXml=src/test/resources/xml/1_client_2_server.xml \
    -DjmeterCsv=src/test/resources/csv/jmeter/10_transactions.csv \
    -DperfmonCsv=src/test/resources/csv/perfmon/2_entries.csv \
    | grep -v "Execution time:" > $ACTUAL_RESULT

sed 's/^\[INFO] //g' $ACTUAL_RESULT | sed '1,29d' | sed '39,$d' > $ACTUAL_RESULT_PARSED

DIFF_OUTPUT=`diff $EXPECTED_RESULT $ACTUAL_RESULT_PARSED`
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