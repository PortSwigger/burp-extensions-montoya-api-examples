/*
 * Copyright (c) 2022. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.trafficredirector;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;

import static burp.api.montoya.http.HttpService.httpService;
import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;

//Burp will auto-detect and load any class that extends BurpExtension.
public class TrafficRedirector implements BurpExtension {
    private static final String HOST_FROM = "host1.example.org";
    private static final String HOST_TO = "host2.example.org";

    @Override
    public void initialize(MontoyaApi api) {
        // set extension name
        api.extension().setName("Traffic redirector");

        // register a new HTTP handler
        api.http().registerHttpHandler(new MyHttpHandler());
    }

    private static class MyHttpHandler implements HttpHandler {
        @Override
        public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
            HttpService service = httpRequestToBeSent.httpService();

            if (HOST_FROM.equalsIgnoreCase(service.host())) {
                HttpRequest newRequest = httpRequestToBeSent.withService(httpService(HOST_TO, service.port(), service.secure()));

                return continueWith(newRequest);
            }

            return continueWith(httpRequestToBeSent);
        }

        @Override
        public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
            return continueWith(httpResponseReceived);
        }
    }
}
