Custom Logger Example Extension
============================

###### Adds a new tab to Burp's UI and displays a log of HTTP traffic for all Burp tools.

 ---

This extension provides a suite-wide HTTP logger within the main Burp UI.

The extension uses the following techniques:
- It creates a custom tab within the main Burp UI, in which to display logging user interface
- It displays a table of items and a read-only editor for requests and responses within a splitpane
- When an item passes through the HttpHandler, it gets added to the table
- You can view the request and response for an item in the table by clicking on the relevant row