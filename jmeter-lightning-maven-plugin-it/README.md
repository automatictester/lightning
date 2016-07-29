# Building this module:

- Build jmeter-lightning-maven-plugin first
- `./src/test/scripts/local/runAllIntegrationTests.sh`
- `mvn clean verify -P jmeter`
- `echo $?`

`echo` prints previous exit code - should be `0`. Failure may indicate problem with the output of JMeter Maven Plugin.