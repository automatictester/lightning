#!/bin/bash

mkdir -p src/test/resources/results/actual/

mvn clean verify \
    -Dmode=verify \
    -DjmeterCsv=src/test/resources/csv/jmeter/10_transactions.csv \
    -DtestSetXml=src/test/resources/xml/1_client_2_server.xml \
    -DperfmonCsv=src/test/resources/csv/perfmon/2_entries.csv \
    > src/test/resources/results/actual/1_client_2_server.txt
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