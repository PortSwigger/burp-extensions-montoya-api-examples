import burp.api.montoya.MontoyaApi;
import burp.api.montoya.websocket.*;

import static burp.api.montoya.websocket.Direction.CLIENT_TO_SERVER;

class MyWebSocketMessageHandler implements MessageHandler {

    private final MontoyaApi api;

    public MyWebSocketMessageHandler(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public TextMessageAction handleTextMessage(TextMessage textMessage) {
        if (textMessage.direction() == CLIENT_TO_SERVER && textMessage.payload().contains("password")) {
            String base64EncodedPayload = api.utilities().base64Utils().encodeToString(textMessage.payload());

            return TextMessageAction.continueWith(base64EncodedPayload);
        }

        return TextMessageAction.continueWith(textMessage);
    }

    @Override
    public BinaryMessageAction handleBinaryMessage(BinaryMessage binaryMessage) {
        return BinaryMessageAction.continueWith(binaryMessage);
    }
}
