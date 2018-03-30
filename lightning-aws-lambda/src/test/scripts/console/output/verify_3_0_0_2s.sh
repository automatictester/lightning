#!/bin/bash

EXPECTED_RESULT="src/test/resources/results/expected/1_client_2_server.txt"
ACTUAL_RESULT="report-combined.log"

rm -f report-1.log report-2.log $ACTUAL_RESULT

aws lambda invoke \
    --function-name Lightning \
    --region eu-west-2 \
    --payload '{"region": "eu-west-2","bucket": "automatictester.co.uk-lightning-aws-lambda","mode": "verify", "xml": "xml/1_client_2_server.xml", "jmeterCsv": "csv/jmeter/10_transactions.csv", "perfmonCsv": "csv/perfmon/2_entries.csv"}' \
    response.json \
    &> /dev/null

S3_OBJECT=`cat response.json | jq -r '.combinedTestReport'`
aws s3 cp s3://automatictester.co.uk-lightning-aws-lambda/${S3_OBJECT} --region eu-west-2 report-1.log &> /dev/null

S3_OBJECT=`cat response.json | jq -r '.teamCityReport'`
aws s3 cp s3://automatictester.co.uk-lightning-aws-lambda/${S3_OBJECT} --region eu-west-2 report-2.log &> /dev/null

cat report-1.log report-2.log > $ACTUAL_RESULT

DIFF_OUTPUT=`diff -B $EXPECTED_RESULT $ACTUAL_RESULT`
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