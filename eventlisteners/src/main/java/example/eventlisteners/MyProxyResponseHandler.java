/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.eventlisteners;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;

public class MyProxyResponseHandler implements ProxyResponseHandler
{
    private final Logging logging;

    public MyProxyResponseHandler(MontoyaApi api)
    {
        logging = api.logging();
    }

    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        logging.logToOutput("Initial intercepted proxy response from " + interceptedResponse.initiatingRequest().httpService());

        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        logging.logToOutput("Final intercepted proxy response from " + interceptedResponse.initiatingRequest().httpService());

        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }

}
