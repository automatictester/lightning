#!/bin/bash

mkdir -p src/test/resources/results/actual/

SHARED="src/test/scripts/shared.sh"
source ${SHARED}

${MVN} clean verify \
    -Dmode=report \
    -DjmeterCsv=src/test/resources/csv/jmeter/2_1_transactions.csv \
    > src/test/resources/results/actual/report_2_1.txt
OUT=$?

echo -e ''; echo `basename "$0"`

if [ $OUT = 1 ];then
    echo "EXIT CODE = $OUT"
    echo "TEST PASSED"
    exit 0
else
    echo "EXIT CODE = $OUT"
    echo "TEST FAILED"
    exit 1
fi