# Building this module:

- Build lightning-standalone first
- Generate JMeter CSV file with JMeter Maven Plugin: `mvn clean verify -Djmx.file=csv-test.jmx -Dresults.file.format=csv`
- `java -jar ../lightning-standalone/target/lightning-standalone-*.jar verify -xml src/test/resources/lightning.xml --jmeter-csv target/jmeter/results/csv-test.csv`
- `echo $?`

- Generate JMeter CSV file with JMeter Maven Plugin: `mvn clean verify -Djmx.file=sdw-test.jmx -Dresults.file.format=xml`
- `java -jar ../lightning-standalone/target/lightning-standalone-*.jar verify -xml src/test/resources/lightning.xml --jmeter-csv target/jmeter/bin/sdw-results.csv`
- `echo $?`

`echo`'s print previous exit codes - should be `0`. Failures may indicate problem with the output of JMeter Maven Plugin. 