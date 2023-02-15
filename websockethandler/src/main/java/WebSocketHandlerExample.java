import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

//Burp will auto-detect and load any class that extends BurpExtension.
public class WebSocketHandlerExample implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("WebSocket Handler Example");

        //Register web socket handler with Burp.
        MyWebSocketCreatedHandler exampleWebSocketCreationHandler = new MyWebSocketCreatedHandler(api);
        api.websockets().registerWebSocketCreatedHandler(exampleWebSocketCreationHandler);
    }
}
