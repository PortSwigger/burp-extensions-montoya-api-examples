import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

//Burp will auto-detect and load any class that extends BurpExtension.
public class ProxyWebSocketHandlerExample implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Proxy Websocket Handler Example");

        //Register proxy web socket handler with Burp.
        MyProxyWebSocketCreationHandler exampleWebSocketCreationHandler = new MyProxyWebSocketCreationHandler();
        api.proxy().registerWebSocketCreationHandler(exampleWebSocketCreationHandler);
    }
}
