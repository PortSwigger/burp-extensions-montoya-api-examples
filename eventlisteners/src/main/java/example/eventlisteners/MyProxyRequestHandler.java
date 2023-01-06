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
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class MyProxyRequestHandler implements ProxyRequestHandler
{
    private final Logging logging;

    public MyProxyRequestHandler(MontoyaApi api)
    {
        logging = api.logging();
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        logging.logToOutput("Initial intercepted proxy request to " + interceptedRequest.httpService());

        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        logging.logToOutput("Final intercepted proxy request to " + interceptedRequest.httpService());

        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}
