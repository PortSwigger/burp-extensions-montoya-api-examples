/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.eventlisteners;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.logging.Logging;

public class MyHttpHandler implements HttpHandler
{
    private final Logging logging;

    public MyHttpHandler(MontoyaApi api)
    {
        logging = api.logging();
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        logging.logToOutput("HTTP request to " + httpRequestToBeSent.httpService() + " [" + httpRequestToBeSent.toolSource().toolType().toolName() + "]");

        return RequestToBeSentAction.continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        logging.logToOutput("HTTP response from " + httpResponseReceived.initiatingRequest().httpService() + " [" + httpResponseReceived.toolSource().toolType().toolName() + "]");

        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }
}