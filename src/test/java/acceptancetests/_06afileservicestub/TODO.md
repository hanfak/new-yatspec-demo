- replace file io impl wiring with test file io which uses a map underneath

## Why

- IO takes a long time
- Using in memeory data structure will be faster to add and retrieve (ie for asseting)
- Can have integration tests, that test io works  with real files
- Can have end to end tests to check that io works for whole flow in app