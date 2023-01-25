Custom Scan Checks Example Extension
============================

###### Provides custom attack insertion points for active scanning.

---

The sample extension demonstrates the following techniques:
- Registering a custom `AuditInsertionPointProvider`
- If the request contains the `data` parameter, it will provide a custom `AuditInsertionPoint`
- The custom `AuditInsertionPoint` will perform the following:
  - Deserialize the data (URL decode and then Base64 decode)
  - Parse the location of the `input=` string withing the decoded data
  - Split the data into a prefix, location to place the payload, and a suffix
  - When building the request with the appropriate payload, it will perform the following:
    - Build the raw data with the appropriate payload
    - Re-serialize the data (Base64 encode then URL encode)