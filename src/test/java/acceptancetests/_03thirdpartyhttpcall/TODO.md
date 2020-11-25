usecase 6
- assert on request sent to service
- captured inputs for request and response to service
- Use wiremock to return response from service
- Use a stub instead
    - intercept call to http client, so it does not send http req
- use jetty filters to for captured inputs and outputs