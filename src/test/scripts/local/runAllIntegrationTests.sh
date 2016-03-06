#!/bin/bash

echo -e 'Run all Integration Tests\n'

mvn clean compile assembly:single

./src/test/scripts/console/output/report_10_0.sh
./src/test/scripts/console/output/verify_1_1_1.sh
./src/test/scripts/console/output/verify_3_0_0.sh
./src/test/scripts/exit/code/regexp.sh
./src/test/scripts/console/output/verify_3_0_0_2s.sh
./src/test/scripts/exit/code/verify_1_1_1.sh
./src/test/scripts/exit/code/verify_3_0_0.sh
./src/test/scripts/exit/code/verify_3_0_0_2s.sh
./src/test/scripts/exit/code/report_2_0.sh
./src/test/scripts/exit/code/report_2_1.sh
./src/test/scripts/exit/code/regexp.sh
