#!/usr/bin/env bash

CORE_MODULE_ROOT_DIR="../../../../lightning-core"
STANDALONE_MODULE_ROOT_DIR="../../../../lightning-standalone"
DATA_GENERATION_SCRIPT_DIR="../../main/groovy"
DATA_GENERATION_MODULE_RESOURCES="../../test/resources"

(cd ${CORE_MODULE_ROOT_DIR} && mvn clean install)
(cd ${STANDALONE_MODULE_ROOT_DIR} && mvn clean compile assembly:single)

# 1550000 records = 50MB
(cd ${DATA_GENERATION_SCRIPT_DIR} && date && groovy JmeterCsvGenerator.groovy 1550000 > ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv && date && ls -lsh ${DATA_GENERATION_MODULE_RESOURCES})
java -jar ${STANDALONE_MODULE_ROOT_DIR}/target/lightning-standalone-*.jar verify -xml ${DATA_GENERATION_MODULE_RESOURCES}/perf-lightning.xml --jmeter-csv ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv

# 15500000 records = 500MB
#(cd ${DATA_GENERATION_SCRIPT_DIR} && date && groovy JmeterCsvGenerator.groovy 15500000 > ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv && date && ls -lsh ${DATA_GENERATION_MODULE_RESOURCES})
#java -jar -Xmx6G ${STANDALONE_MODULE_ROOT_DIR}/target/lightning-standalone-*.jar verify -xml ${DATA_GENERATION_MODULE_RESOURCES}/perf-lightning.xml --jmeter-csv ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv
