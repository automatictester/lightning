#!/bin/bash

mkdir -p src/test/resources/results/actual/

java \
    -jar target/lightning*.jar \
    report \
    --jmeter-csv src/test/resources/csv/jmeter/2_transactions.csv \
    &> src/test/resources/results/actual/report_1_1.txt
OUT=$?

echo -e ''; echo `basename "$0"`

if [ $OUT = 0 ];then
    echo "EXIT CODE = $OUT"
    echo "TEST PASSED"
    exit 0
else
    echo "EXIT CODE = $OUT"
    echo "TEST FAILED"
    exit 1
fi