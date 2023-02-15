import burp.api.montoya.proxy.websocket.ProxyWebSocketCreation;
import burp.api.montoya.proxy.websocket.ProxyWebSocketCreationHandler;

class MyProxyWebSocketCreationHandler implements ProxyWebSocketCreationHandler {

    @Override
    public void handleWebSocketCreation(ProxyWebSocketCreation webSocketCreation) {
        webSocketCreation.proxyWebSocket().registerProxyMessageHandler(new MyProxyWebSocketMessageHandler());
    }
}
