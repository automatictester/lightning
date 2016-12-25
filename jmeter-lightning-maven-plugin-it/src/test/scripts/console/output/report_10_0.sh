#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/report.txt"
ACTUAL_RESULT="src/test/resources/results/actual/report.txt"
ACTUAL_RESULT_PARSED="src/test/resources/results/actual/report_parsed.txt"

SHARED="src/test/scripts/shared.sh"
source ${SHARED}

${MVN} clean verify \
    -Dmode=report \
    -DjmeterCsv=src/test/resources/csv/jmeter/10_transactions.csv \
    > $ACTUAL_RESULT

sed 's/^\[INFO] //g' $ACTUAL_RESULT | egrep -i "transaction"  > $ACTUAL_RESULT_PARSED

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
    echo $ACTUAL_RESULT
    cat $ACTUAL_RESULT
    echo $ACTUAL_RESULT_PARSED
    cat $ACTUAL_RESULT_PARSED
    exit 1
fi