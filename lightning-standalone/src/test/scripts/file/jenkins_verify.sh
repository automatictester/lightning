#!/bin/bash

rm -f lightning-jenkins.properties

EXPECTED_RESULT="src/test/resources/results/expected/jenkins/3_2.txt"
ACTUAL_RESULT="lightning-jenkins.properties"

java \
    -jar target/lightning*.jar \
    verify \
    -xml src/test/resources/xml/1_1_1.xml \
    --jmeter-csv src/test/resources/csv/jmeter/10_transactions.csv \
    &> /dev/null

DIFF_OUTPUT=`diff $EXPECTED_RESULT $ACTUAL_RESULT`
OUT=$?

echo -e ''; echo `basename "$0"`

if [ $OUT -eq 1 ];then
    echo "OUTPUT AS EXPECTED"
    echo "TEST PASSED"
    exit 0
else
    echo "INCORRECT CONSOLE OUTPUT - DIFF:"
    echo $DIFF_OUTPUT
    echo "TEST FAILED"
    exit 1
fi