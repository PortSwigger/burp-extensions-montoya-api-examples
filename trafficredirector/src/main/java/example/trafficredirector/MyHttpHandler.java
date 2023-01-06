/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.trafficredirector;

import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;

import static burp.api.montoya.http.HttpService.httpService;
import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;

public class MyHttpHandler implements HttpHandler
{
    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        HttpService service = httpRequestToBeSent.httpService();

        if (TrafficRedirector.HOST_FROM.equalsIgnoreCase(service.host())) {
            HttpRequest newRequest = httpRequestToBeSent.withService(httpService(TrafficRedirector.HOST_TO, service.port(), service.secure()));

            return continueWith(newRequest);
        }

        return continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }
}