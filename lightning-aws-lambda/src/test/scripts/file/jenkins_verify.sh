#!/bin/bash

rm -f jenkins-lightning.properties

EXPECTED_RESULT="src/test/resources/results/expected/jenkins/3_2.txt"
ACTUAL_RESULT="jenkins-lightning.properties"

aws lambda invoke \
    --function-name Lightning \
    --region eu-west-2 \
    --payload '{"region": "eu-west-2", "bucket": "automatictester.co.uk-lightning-aws-lambda", "mode": "verify", "xml": "xml/1_1_1.xml", "jmeterCsv": "csv/jmeter/10_transactions.csv"}' \
    response.json \
    &> /dev/null

S3_OBJECT=`cat response.json | jq -r '.jenkinsReport'`

aws s3 cp s3://automatictester.co.uk-lightning-aws-lambda/${S3_OBJECT} --region eu-west-2 jenkins-lightning.properties &> /dev/null

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