[![Join the chat at https://gitter.im/automatictester/lightning](https://badges.gitter.im/automatictester/lightning.svg)](https://gitter.im/automatictester/lightning?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://snap-ci.com/automatictester/lightning/branch/master/build_image)](https://snap-ci.com/automatictester/lightning/branch/master)
[![codecov](https://codecov.io/gh/automatictester/lightning/branch/master/graph/badge.svg)](https://codecov.io/gh/automatictester/lightning)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning)

### Introduction

Lightning integrates JMeter non-functional tests with your CI/CD server. It analyses results of your JMeter tests and provides your server with unequivocal information allowing it to make autonomous decision whether to pass or fail the build - with no human involvement needed.

Lightning is the **most advanced** tool providing that kind of integration, and this is not just a marketing slogan. Here is why:

:white_check_mark: Lightning allows you to make a decision on performance build results, based on very broad range of client-side statistics. These include: throughput, average response time, median response time, maximum response time, n-th percentile response time, response time standard deviation and percentage of failed transactions. Learn more about available [test types](https://github.com/automatictester/lightning/wiki/Test-Types).

:white_check_mark: There is no need to configure anything on CI/CD server side to run the above tests. You can have your [first Lightning test](https://github.com/automatictester/lightning/wiki/Quick-Start-Guide) running in minutes.

:white_check_mark: Lightning understands you may have different SLA different transactions types, and therefore allows you to define different thresholds for every transaction type.

:white_check_mark: Lightning is not only about client-side statistics. It gives you unique ability to measure [server-side metrics](https://github.com/automatictester/lightning/wiki/Server-Side-Tests) (CPU, memory, network usage etc) and fail the build if certain metrics go above defined threshold. Or below. Your JMeter tests are no longer pure performance tests, they are now powerful non-functional tests. This lets you test load balancing and other system characteristics.

:white_check_mark: No disk space considerations - there is no need for extremely verbose JMeter results in XML format. Lightning processes JMeter results in [CSV format](https://github.com/automatictester/lightning/wiki/Configure-JMeter-to-Save-Relevant-Data) with just a few columns.

:white_check_mark: No security concerns - your data is not uploaded to any 3rd party servers.

:white_check_mark: Lightning integrates seamlessly with CI/CD servers using "language" which they understand - [exit code](https://github.com/automatictester/lightning/wiki/CI-CD-Server-Integration).

:white_check_mark: Lightning is a standalone tool, not a plugin. If you change your CI server or build tool, you can still use it.

:white_check_mark: Lightning is open source and free for commercial and non-commercial use.

:white_check_mark: If all you need is number of executed an failed JMeter transactions, Lightning can check that for you [as well](https://github.com/automatictester/lightning/wiki/Report-Mode).

:white_check_mark: If you use [Jenkins](https://github.com/automatictester/lightning/wiki/Enhanced-Jenkins-Integration), you can set the build name to something more meaningful than the defaults. Build name can now include number of passed and failed tests.

:white_check_mark: If you use [TeamCity](https://github.com/automatictester/lightning/wiki/Enhanced-TeamCity-Integration), you will benefit from build statistics charts.

:white_check_mark: If you want to [experiment](https://github.com/automatictester/lightning/wiki/Java-API-%28experimental%29) with calling Lightning directly from your Java code, it's available for download from Maven Central.

### Download

Standalone JAR is available for download from [Releases](https://github.com/automatictester/lightning/releases) tab.

### Changelog

All new features and bugfixes are included in [release notes](https://github.com/automatictester/lightning/releases).

### Philosophy

- Keep technology stack as close to JMeter as possible - Lightning is 100% Java
- Be continuous integration server-independent and operating system-independent. Lightning should not be designed to run only in a particular environment, but can offer extra features for certain environments
- Using Lightning shouldn't require coding skills, as JMeter doesn't require that neither
- Release changes frequently
- Be well documented
- Be well tested
- Do not provide bugfixes and support for old versions
- Follow [SemVer](http://semver.org)

### Design assumptions

- JMeter and PerfMon CSV files produced in CI environment should be small enough to be processed by Lightning and stored in memory without hacks

### Project maturity

* Lightning as a standalone JAR has been used in day-to-day delivery for months and client-side tests can be considered production-ready.
* Server-side tests is a new functionality.

### Issues, questions and feature requests

Issues, questions and feature requests can be raised on [Gitter](https://gitter.im/automatictester/lightning) or using our [issue tracker](https://github.com/automatictester/lightning/issues).

### Contributors

All the information you may need (and even more) can be found in our [wiki](https://github.com/automatictester/lightning/wiki/Info-for-Contributors). Pull requests are welcome!

### License

Released under the MIT license.
