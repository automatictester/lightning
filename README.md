[![Join the chat at https://gitter.im/automatictester/lightning](https://badges.gitter.im/automatictester/lightning.svg)](https://gitter.im/automatictester/lightning?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://snap-ci.com/automatictester/lightning/branch/master/build_image)](https://snap-ci.com/automatictester/lightning/branch/master)
[![codecov](https://codecov.io/gh/automatictester/lightning/branch/master/graph/badge.svg)](https://codecov.io/gh/automatictester/lightning)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/lightning)

### Introduction

Lightning integrates JMeter non-functional tests with Continuous Integration infrastructure. Lightning analyses results of your non-functional tests and provides your CI server with simple and meaningful information whether to pass or fail the build - with no human involvement needed. Check the [story behind Lightning](https://github.com/automatictester/lightning/wiki/Story-Behind-Lightning-1.0) for more information.

### Download

Lightning is a 100% Java tool. It is available as a standalone JAR for download from [Releases](https://github.com/automatictester/lightning/releases) tab.

### Changelog

All new features and bugfixes are included in [release notes](https://github.com/automatictester/lightning/releases).

### Philosophy

- Keep technology stack as close to JMeter as possible
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
