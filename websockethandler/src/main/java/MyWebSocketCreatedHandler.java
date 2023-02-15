import burp.api.montoya.MontoyaApi;
import burp.api.montoya.websocket.WebSocketCreated;
import burp.api.montoya.websocket.WebSocketCreatedHandler;

class MyWebSocketCreatedHandler implements WebSocketCreatedHandler {

    private final MontoyaApi api;

    MyWebSocketCreatedHandler(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public void handleWebSocketCreated(WebSocketCreated webSocketCreated) {
        webSocketCreated.webSocket().sendTextMessage("First Message");

        webSocketCreated.webSocket().registerMessageHandler(new MyWebSocketMessageHandler(api));
    }
}
