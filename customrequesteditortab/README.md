Custom Request Editor Tab Example Extension
============================

###### Adds a new tab to Burp's HTTP message editor, in order to handle a data serialization format

 ---

This extension provides a new tab on the message editor for requests that contain a specified parameter.

The extension uses the following techniques:
- It creates a custom request tab on the message editor, provided that the `data` parameter is present
- If it is appropriate, the editor is set to be read-only
- The value of the `data` parameter is deserialized (URL decoded, then Base64 decoded) and displayed in the custom tab
- If the value of the data is modified, the content will be re-serialized (Base64 encoded then URL encoded) and updated in the HttpRequest