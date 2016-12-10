#!/bin/bash

echo -e '\nRun all Integration Tests'

rm -rf src/test/resources/results/actual/

./src/test/scripts/console/output/report_10_0.sh
./src/test/scripts/console/output/verify_1_1_1.sh
./src/test/scripts/console/output/verify_3_0_0.sh
./src/test/scripts/console/output/verify_3_0_0_2s.sh
./src/test/scripts/console/output/verify_regexp.sh
./src/test/scripts/exit/code/report_2_0.sh
./src/test/scripts/exit/code/report_2_1.sh
./src/test/scripts/exit/code/verify_1_1_1.sh
./src/test/scripts/exit/code/verify_3_0_0.sh
./src/test/scripts/exit/code/verify_3_0_0_2s.sh
./src/test/scripts/exit/code/verify_regexp.sh
./src/test/scripts/file/junit.sh