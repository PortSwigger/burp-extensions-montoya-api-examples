Context Menu Example Extension
============================

###### Registers new context menu items to print requests and responses.

---
This extension adds a new context menu item to print out the request or response of an HttpRequestResponse in the Target, Proxy or Logger tab.

The sample extension demonstrates the following techniques:
- Registering a new `ContextMenuItemsProvider`.
- Creating a `JMenuItem`.
- Adding an action listener to a `JMenuItem`.
- If you right-click in a message editor context, it will use the item from the message editor.
- If you right-click on a table item, it will print the request/response for the first selected item.