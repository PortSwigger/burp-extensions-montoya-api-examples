package example.proxyhandler;

import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToSendAction;

import static burp.api.montoya.core.HighlightColor.BLUE;

class MyProxyHttpResponseHandler implements ProxyResponseHandler {
    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        //Highlight all responses that have username in them
        if (interceptedResponse.bodyToString().contains("username")) {
            return ProxyResponseReceivedAction.continueWith(interceptedResponse, interceptedResponse.annotations().withHighlightColor(BLUE));
        }

        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToSendAction handleResponseToSend(InterceptedResponse interceptedResponse) {
        return ProxyResponseToSendAction.continueWith(interceptedResponse);
    }
}
