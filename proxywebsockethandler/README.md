Proxy WebSocket Handler Example Extension
=========================================

###### Demonstrates performing various actions on websocket messages passing through the Proxy

 ---

The extension works as follows:
- It registers a Proxy web socket creation handler
- When a Proxy web socket is created
  - It registers a proxy web socket message listener for the created websocket
  - Any message that contains the text "username" is highlighted in red.
  - Any message from the client that contains the text "password" is force intercepted.
