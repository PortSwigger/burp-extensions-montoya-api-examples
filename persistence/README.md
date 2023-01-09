Persistence Example Extension
============================

###### Demonstrates saving and loading data to the project file.

 ---

The extension works as follows:
- It saves and loads a simple incrementing integer value with the project file.
  - Sends to the event log how many times Burp or the extension were restarted.
- It saves and loads extension built http requests
  - Sends the  requests to repeater with an incrementing tab counter when Burp is restarted or the extension is reloaded.
- It saves the last 5 requests/responses issued by Burp
  - Prints the last 5 request/respones to the output log when burp is restarted or the extension is reloaded. 
  - Uses Persisted Lists to automatically save/load data stored in the list.