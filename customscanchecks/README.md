Custom Scan Checks Example Extension
============================

###### Implements custom checks to extend the capabilities of Burp's active and passive scanning engines.

---

The sample extension demonstrates the following techniques:
- Registering a custom scan check
- Performing passive and active scanning when initiated by the user
- Using the Burp-provided `AuditInsertionPoint` to construct requests for active scanning using specified payloads
- Using a helper method to search responses for relevant match strings
- Providing an `MarkedHttpRequestResponse` to highlight relevant portions of requests and responses, 
- Synchronously reporting custom scan issues in response to the relevant checks.
- Guiding Burp on when to consolidate duplicated issues at the same URL (e.g., when the user has scanned the same item multiple times).