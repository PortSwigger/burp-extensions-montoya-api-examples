Custom Session Tokens Example Extension
============================

###### Demonstrates working with custom session tokens that Burp doesn't normally understand.

 ---

This example demonstrates how you can couple a recorded macro with an extension to automatically gain a session token for a website and use it in later requests that Burp makes.

The macro mechanism that Burp provides allows you to record the request triggering creation of a session made via the proxy.

The extension uses the following techniques:
- Registers a `SessionHandlingAction` handler
- Fetches the list of macro requests and responses
- Extracts the response headers from the last `HttprequestResponse` item in the list
- Finds the relevant session header (in this example, this header is `X-Custom-Session-Id`)
- Returns an `HttpRequest` with an updated session header