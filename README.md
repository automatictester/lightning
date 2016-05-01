[![Join the chat at https://gitter.im/automatictester/lightning](https://badges.gitter.im/automatictester/lightning.svg)](https://gitter.im/automatictester/lightning?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://snap-ci.com/automatictester/lightning/branch/master/build_image)](https://snap-ci.com/automatictester/lightning/branch/master)
[![codecov](https://codecov.io/gh/automatictester/lightning/branch/master/graph/badge.svg)](https://codecov.io/gh/automatictester/lightning)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning)

### Introduction

Lightning integrates JMeter non-functional tests with your CI/CD server. It analyses results of your JMeter tests and provides your server with unequivocal information allowing it to make autonomous decision whether to pass or fail the build - with no human involvement needed.

Lightning is the most advanced tool providing that kind of integration and this is not just a slogan. Here is why:

:white_check_mark: Lightning allows you to make a decision on performance build results, based on very broad range of client-side statistics. These include: throughput, average response time, median response time, maximum response time, n-th percentile response time, response time standard deviation and percentage of failed transactions. Learn more about available [test types](https://github.com/automatictester/lightning/wiki/Test-Types).

:white_check_mark: There is no need to configure anything on CI/CD server side to run the above tests. You can have your [first Lightning test](https://github.com/automatictester/lightning/wiki/Quick-Start-Guide) running in minutes.

:white_check_mark: Lightning understands you may have different SLA different transactions types, and therefore allows you to define different thresholds for every transaction type.

:white_check_mark: Lightning is not only about client-side statistics. It gives you unique ability to measure [server-side metrics](https://github.com/automatictester/lightning/wiki/Server-Side-Tests) (CPU, memory, network usage etc) and fail the build if certain metrics go above defined threshold. Or below. Your JMeter tests are no longer pure performance tests, they are now powerful non-functional tests. This lets you test load balancing and other system characteristics.

:white_check_mark: No disk space considerations - there is no need for extremely verbose JMeter results in XML format. Lightning processes JMeter results in [CSV format](https://github.com/automatictester/lightning/wiki/Configure-JMeter-to-Save-Relevant-Data) with just a few columns.

:white_check_mark: No security concerns - your data is not uploaded to any 3rd party servers.

:white_check_mark: Lightning integrates seamlessly with CI/CD servers using "language" which they understand - [exit code and JUnit XML report](https://github.com/automatictester/lightning/wiki/CI-CD-Server-Integration).

:white_check_mark: Lightning is a standalone tool, not a plugin. If you change your CI server or build tool, you can still use it.

:white_check_mark: Lightning is open source and free for commercial and non-commercial use.

:white_check_mark: If all you need is number of executed an failed JMeter transactions, Lightning can check that for you [as well](https://github.com/automatictester/lightning/wiki/Report-Mode).

:white_check_mark: If you use [Jenkins](https://github.com/automatictester/lightning/wiki/Enhanced-Jenkins-Integration), you can set the build name to something more meaningful than the defaults. Build name can now include number of passed and failed tests.

:white_check_mark: If you use [TeamCity](https://github.com/automatictester/lightning/wiki/Enhanced-TeamCity-Integration), you will benefit from build statistics charts.

:white_check_mark: If you want to [experiment](https://github.com/automatictester/lightning/wiki/Java-API-%28experimental%29) with calling Lightning directly from your Java code, it's available for download from Maven Central.

### Quick start guide

Preconditions:
- Check your Java version with `java -version`. Lightning requires Java 7 or above.
- Download most recent `lightning-standalone-<version>.jar` from [releases](https://github.com/automatictester/lightning/releases).
- Configure your JMeter tests to [save relevant data](https://github.com/automatictester/lightning/wiki/Configure-JMeter-to-Save-Relevant-Data).

You can define any combination of tests described in [test types](https://github.com/automatictester/lightning/wiki/Test-Types) and execute them executed against JMeter output.

In a typical scenario Lightning requires 2 sources of input data: XML config file and JMeter CSV output. XML file contains definition of Lightning tests, which will be executed to determine if execution should be marked as passed or failed.

Lightning XML config file, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<testSet>
    <avgRespTimeTest>
        <testName>Average login times</testName>
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

Run Lightning:

`java -jar lightning-<version>.jar verify -xml=path/to/xml/file --jmeter-csv=path/to/csv/file`

Sample output:

```
Test name:            Average login times
Test type:            avgRespTimeTest
Transaction name:     Login
Expected result:      Average response time <= 4000
Actual result:        Average response time = 3583.2
Transaction count:    5
Longest transactions: [3780, 3700, 3514, 3474, 3448]
Test result:          Pass
```

### Changelog

All new features and bugfixes are included in [release notes](https://github.com/automatictester/lightning/releases).

### Project maturity

Lightning as a standalone JAR has been used in day-to-day delivery for months and client-side tests can be considered production-ready. Server-side tests is a new functionality.

### Issues, questions and feature requests

Issues, questions and feature requests can be raised on [Gitter](https://gitter.im/automatictester/lightning) or using our [issue tracker](https://github.com/automatictester/lightning/issues).

### Contributors

All the information you may need (and even more) can be found in our [wiki](https://github.com/automatictester/lightning/wiki/Info-for-Contributors). Pull requests are welcome!

### License

Released under the MIT license.
