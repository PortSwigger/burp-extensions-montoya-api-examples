WebSocket Handler Example Extension
===================================

###### Demonstrates performing various actions on websocket messages passing through any tool in Burp

 ---

The extension works as follows:
- It registers a web socket created handler
- When a web socket is created
  - It sends an initial text message
  - It registers a message listener for the websocket
  - Any message from the client that contains the text "password" is base64 encoded.
