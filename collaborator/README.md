Collaborator Example Extension
============================

###### Demonstrates using Collaborator and Persistence functionality.

---
This extension demonstrates creating a `CollaboratorClient`, polling the Collaborator Server and saving the Collaborator `SecretKey` to the project file using Persistence.

The sample extension demonstrates the following techniques:
- Creating a Collaborator Client
  - If the extension has been previously loaded in the project file, it restores the previously used `CollaboratorClient`
  - If this is the first time the extension has been loaded in this project file, it creates a new `CollaboratorClient` and saves the `SecretKey` to the project file
- Registers a `ProxyRequestHandler` to insert Collaborator payloads into requests
- Polls the Collaborator Server for asynchronous interactions
- Logs any discovered interactions to the extension Output tab
- When the extension is unloaded or Burp Suite is closed, the extension stops polling the Collaborator server

You can test this extension on the following URL: https://portswigger-labs.net/ssrf-dns.php

The `poller` package has been designed to be easily reused in other extensions.