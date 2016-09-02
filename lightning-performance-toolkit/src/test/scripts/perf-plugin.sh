#!/usr/bin/env bash

CORE_MODULE_ROOT_DIR="../../../../lightning-core"
PLUGIN_MODULE_ROOT_DIR="../../../../jmeter-lightning-maven-plugin"
PLUGIN_MODULE_IT_ROOT_DIR="../../../../jmeter-lightning-maven-plugin-it"
DATA_GENERATION_SCRIPT_DIR="../../main/groovy"
DATA_GENERATION_MODULE_RESOURCES="../../test/resources"
DATA_GENERATION_MODULE_RESOURCES_FROM_PLUGIN_MODULE_IT_ROOT_DIR="../../lightning/lightning-performance-toolkit/src/test/resources"

(cd ${CORE_MODULE_ROOT_DIR} && mvn clean install)
(cd ${PLUGIN_MODULE_ROOT_DIR} && mvn clean install)

# 1550000 records = 50MB
(cd ${DATA_GENERATION_SCRIPT_DIR} && date && groovy JmeterCsvGenerator.groovy 1550000 > ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv && date && ls -lsh ${DATA_GENERATION_MODULE_RESOURCES})
(cd ${PLUGIN_MODULE_IT_ROOT_DIR} && mvn clean verify -Dmode=verify -DtestSetXml=${DATA_GENERATION_MODULE_RESOURCES_FROM_PLUGIN_MODULE_IT_ROOT_DIR}/perf-lightning.xml -DjmeterCsv=${DATA_GENERATION_MODULE_RESOURCES_FROM_PLUGIN_MODULE_IT_ROOT_DIR}/generated.csv)

# 15500000 records = 500MB
#export MAVEN_OPTS="-Xmx6G"
#(cd ${DATA_GENERATION_SCRIPT_DIR} && date && groovy JmeterCsvGenerator.groovy 15500000 > ${DATA_GENERATION_MODULE_RESOURCES}/generated.csv && date && ls -lsh ${DATA_GENERATION_MODULE_RESOURCES})
#(cd ${PLUGIN_MODULE_IT_ROOT_DIR} && mvn clean verify -DargLine="-Xmx6G" -Dmode=verify -DtestSetXml=${DATA_GENERATION_MODULE_RESOURCES_FROM_PLUGIN_MODULE_IT_ROOT_DIR}/perf-lightning.xml -DjmeterCsv=${DATA_GENERATION_MODULE_RESOURCES_FROM_PLUGIN_MODULE_IT_ROOT_DIR}/generated.csv)
