TODO

- one external call
- multiple ext call to same services
- multiple ext call to different services
- one external call and database useage


usecase 6
- assert on request sent to service
- captured inputs for request and response to service
- Use wiremock to return response from service
- Use a stub instead
    - intercept call to http client, so it does not send http req
- use jetty filters to for captured inputs and outputs

## why

- Actual calls to http end point, but wiremock intercepts them and stubs out response wiht what you want (done in the
 givens)
- Allows us to see http interactions
- can use test http client instead of real http client, which can be primable, and can create the http req and resp
 (From the givens) which will be logged. so no actual http calls
    - faster then making http call
    - will need extra integration and end to end with wiremock to test http call
- This is not a sub for doing actual integration testing with a real third party, hopefully using a test server
 supplied by the 3rd party or in production (using specific setup for testing and not customers)