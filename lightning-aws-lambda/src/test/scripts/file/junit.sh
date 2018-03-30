#!/bin/bash

rm -f junit.xml

EXPECTED_RESULT="src/test/resources/results/expected/junit/junit_expected.xml"
ACTUAL_RESULT="junit.xml"

aws lambda invoke \
    --function-name Lightning \
    --region eu-west-2 \
    --payload '{"region": "eu-west-2","bucket": "automatictester.co.uk-lightning-aws-lambda","mode": "verify","jmeterCsv": "csv/jmeter/2_transactions.csv","perfmonCsv": "csv/perfmon/junit_report.csv","xml": "xml/junit_report.xml"}' \
    response.json \
    &> /dev/null

S3_OBJECT=`cat response.json | jq -r '.junitReport'`

aws s3 cp s3://automatictester.co.uk-lightning-aws-lambda/${S3_OBJECT} --region eu-west-2 junit.xml &> /dev/null

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