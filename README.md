# Overview

[![Build status](https://api.travis-ci.org/automatictester/lightning.svg?branch=master)](https://travis-ci.org/automatictester/lightning)
[![Coverage Status](https://coveralls.io/repos/automatictester/lightning/badge.svg?branch=master)](https://coveralls.io/r/automatictester/lightning?branch=master)

### Breaking changes in version 2

Command line parameter **-csv** is now renamed to **--jmeter-csv**. If you want to upgrade but don't need to use server side monitoring functionality, just update this parameter name in your Lightning startup script.

### Download

Lightning as standalone JAR is available for download from [Releases](https://github.com/automatictester/lightning/releases) tab.

Lightning as a dependency is available in [Maven Central](http://search.maven.org/#search|gav|1|g%3A%22uk.co.automatictester%22%20AND%20a%3A%22lightning%22).

### Changelog

All new features and bugfixes are included in [release notes](https://github.com/automatictester/lightning/releases).

### Goals

Lightning's goal is to revolutionise the approach to non-functional testing. Lightning integrates JMeter non-functional tests with Continuous Integration infrastructure. It can instantly provide CI server with simple and meaningful information whether to pass or fail the build - with no human involvement needed. Check the [story behind Lightning](https://github.com/automatictester/lightning/wiki/Story-Behind-Lightning) for more information.

### Philosophy

- Keep technology stack as close to JMeter as possible
- Be continuous integration server-independent and operating system-independent. Lightning should not be designed to run in particular environment only, but can offer extra features for certain environments
- Using Lightning shouldn't require coding skills, as JMeter doesn't require that neither
- Release changes frequently
- Be well documented
- Be well tested
- Do not provide bugfixes and support for old versions
- Follow [SemVer](http://semver.org)

### Design assumptions

- JMeter and PerfMon CSV files produced in CI environment should be small enough to be processed by Lightning and stored in memory without hacks

### Project maturity

* Lightning as a standalone JAR has been used in day-to-day delivery for months and can be considered production-ready.
* Server-side tests is a new functionality.
* Lightning as a Java dependency is used only by a subset of users.

### Future

Lightning 2 delivered new functionality to measure server-side metrics. This brand new option could benefit from a few tweaks, so stay tuned!

# How to start

You can start using Lightning as a standalone JAR or directly from Java code.

### Standalone JAR

Using Lightning as a standalone JAR is a most suitable option for most users.

By default, continuous integration servers mark build as failed based on exit code of executed build step. This behaviour can be amended using CI server-specific features or plugins, which allow to fail build based on build console output.

There are two modes of Lightning execution: `verify` and `report`. Differences between those two are described in the below sections. To make things simple, Lightning running in `verify` mode exits with code 0 if none of the defined tests failed, and 1 if any of the tests failed. Lightning running in `report` mode exits with code 0 if there were no failed JMeter transactions, and 2 if any of JMeter transactions failed. If for some reason you prefer to parse Lightning output, you can do that as well. Lightning offers some extra features for [TeamCity](https://github.com/automatictester/lightning/wiki/Enhanced-TeamCity-Integration) and [Jenkins](https://github.com/automatictester/lightning/wiki/Enhanced-Jenkins-Integration) users.

### Preconditions

- Check your Java version with `java -version`. Lightning requires Java 7 or above.
- Download most recent `lightning-standalone-<version>.jar` from [releases](https://github.com/automatictester/lightning/releases).
- Configure your JMeter tests to [save relevant data](https://github.com/automatictester/lightning/wiki/Configure-JMeter-to-Save-Relevant-Data).

### Verify mode

This is the mode used is most scenarios. Miscellaneous tests described in [test types](https://github.com/automatictester/lightning/wiki/Test-Types) are executed agains JMeter output. Lightning returns 1 exit code on non-zero number of failed tests.

In `verify` mode, Lightning requires at least 2 sources of input data: XML config file and JMeter CSV output. XML file contains definition of tests, which will be executed to determine if execution should be marked as passed or failed, based on analysis of JMeter CSV output.

Lightning XML config file, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<testSet>
    <avgRespTimeTest>
        <testName>Test #1</testName>
        <description>Verify average login times</description>
        <transactionName>Login</transactionName>
        <maxAvgRespTime>4000</maxAvgRespTime>
    </avgRespTimeTest>
</testSet>
```

JMeter CSV output file, e.g.:

```
timeStamp,elapsed,label,responseCode,threadName,dataType,success,bytes,Latency
1434291247743,3514,Login,200,Thread Group 1-2,,true,444013,0
1434291247541,3780,Login,200,Thread Group 1-1,,true,444236,0
1434291247949,3474,Login,200,Thread Group 1-3,,true,444041,0
1434291248160,3448,Login,200,Thread Group 1-4,,true,444712,0
1434291248359,3700,Login,200,Thread Group 1-5,,true,444054,0
1434291251330,10769,Search,200,Thread Group 1-1,,true,1912363,0
1434291251624,10626,Search,200,Thread Group 1-4,,true,1912352,0
1434291251436,11086,Search,200,Thread Group 1-3,,true,1912321,0
1434291251272,11250,Search,200,Thread Group 1-2,,true,1912264,0
1434291252072,11221,Search,200,Thread Group 1-5,,true,1912175,0
```

To run Lightning:

`java -jar lightning-<version>.jar verify -xml=path/to/xml/file --jmeter-csv=path/to/csv/file`

Sample output:

```
Test name:        Test #1
Test description: Verify average login times
Transaction name: Login
Expected result:  Average response time <= 4000
Actual result:    Average response time = 3583.2
Test result:      Pass
```

#### Using server-side tests

To include server-side tests in `verify` mode, Lightning requires 3 sources of input data: XML config file, JMeter and PerfMon CSV output files. XML file contains definition of tests, which will be executed to determine if execution should be marked as passed or failed, based on analysis of CSV files. If you want to run server-side tests, you should also have at least one test analysing client-side metrics (e.g. average response times). This way you not only monitor server load, but also make sure the client gets what expected.

To run server-side tests, you need to configure your JMeter test to collect server-side statistics using [PerfMon Server Agent](http://jmeter-plugins.org/wiki/PerfMonAgent/) and [JMeter PerfMon Metrics Collector](http://jmeter-plugins.org/wiki/PerfMon/).

Lightning XML config file, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<testSet>
    <avgRespTimeTest>
        <testName>Test #1</testName>
        <description>Verify average login times</description>
        <transactionName>Login</transactionName>
        <maxAvgRespTime>4000</maxAvgRespTime>
    </avgRespTimeTest>
    <serverSideTest>
        <testName>Test #2</testName>
        <subType>LESS_THAN</subType>         
        <description>Verify server-side resource utilisation</description>         
        <hostAndMetric>192.168.0.12 CPU</hostAndMetric>
        <metricValueA>60000</metricValueA>
    </serverSideTest>
</testSet>
```

JMeter CSV output file, e.g.:

```
timeStamp,elapsed,label,responseCode,threadName,dataType,success,bytes,Latency
1434291247743,3514,Login,200,Thread Group 1-2,,true,444013,0
1434291247541,3780,Login,200,Thread Group 1-1,,true,444236,0
1434291247949,3474,Login,200,Thread Group 1-3,,true,444041,0
1434291248160,3448,Login,200,Thread Group 1-4,,true,444712,0
1434291248359,3700,Login,200,Thread Group 1-5,,true,444054,0
1434291251330,10769,Search,200,Thread Group 1-1,,true,1912363,0
1434291251624,10626,Search,200,Thread Group 1-4,,true,1912352,0
1434291251436,11086,Search,200,Thread Group 1-3,,true,1912321,0
1434291251272,11250,Search,200,Thread Group 1-2,,true,1912264,0
1434291252072,11221,Search,200,Thread Group 1-5,,true,1912175,0
```

PerfMon CSV output file, e.g.:

```
1434291247949,9128,192.168.0.12 CPU,,,,,true,0,0,0,0
1434291251436,21250,192.168.0.12 CPU,,,,,true,0,0,0,0
```

To run Lightning:

`java -jar lightning-<version>.jar verify -xml=path/to/xml/file --jmeter-csv=path/to/jmeter/csv/file --perfmon-csv=path/to/perfmon/csv/file`

Sample output:

```
Test name:            Test #1
Test type:            avgRespTimeTest
Test description:     Verify average login times
Transaction name:     Login
Expected result:      Average response time <= 4000
Actual result:        Average response time = 3583.2
Transaction count:    5
Test result:          Pass

Test name:            Test #2
Test type:            serverSideTest
Test subtype:         Less than
Test description:     Verify server-side resource utilisation
Host and metric:      192.168.0.12 CPU
Expected result:      Average value < 60000
Actual result:        Average value = 15189.0
Entries count:        2
Test result:          Pass
```

Find out more about the available options [here](https://github.com/automatictester/lightning/wiki/Test-Types#server-side-test).


### Report mode

In this mode, JMeter doesn't execute any tests. It only parses JMeter output and reports total number of transactions and number of failed transactions. Lightning returns 2 exit code on non-zero number of failed transactions.

In `report` mode, Lightning requires only 1 source of input data: JMeter CSV output. In this mode Lightning parses JMeter output and reports total number of transactions and number of failed transactions. In this mode, Lightning doesn't perform any verification.

To run Lightning:

`java -jar lightning-<version>.jar report --jmeter-csv=path/to/csv/file`

Sample output:

`Transactions executed: 10, failed: 0`

### Java API

Advanced Lightning users may want to call its Java API directly. It doesn't provide any extra features over standalone JAR, but gives you more low-level control. Here you can find examples of calling Lightning Java API in [verify](https://github.com/automatictester/lightning-java-api-tests/blob/master/src/test/java/uk/co/automatictester/lightning/java/api/tests/VerifyTest.java) and [report](https://github.com/automatictester/lightning-java-api-tests/blob/master/src/test/java/uk/co/automatictester/lightning/java/api/tests/ReportTest.java) mode.

# Misc

### Issues, questions and feature requests

Issues, questions and feature requests should be raised [here](https://github.com/automatictester/lightning/issues).

### Contributors

All the information you may need (and even more) can be found [here](https://github.com/automatictester/lightning/wiki/Info-for-Contributors). Pull requests are welcome!

### License

Released under the MIT license.
