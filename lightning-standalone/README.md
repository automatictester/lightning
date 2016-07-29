# Building this module:

- Build lightning-core first
- `mvn clean compile test assembly:single`
- `./src/test/scripts/local/runAllIntegrationTests.sh`

The above will test Lightning Standalone and provide binary pass/fail results.
For additional level of confidence, run end-to-end tests in lightning-standalone-it module.