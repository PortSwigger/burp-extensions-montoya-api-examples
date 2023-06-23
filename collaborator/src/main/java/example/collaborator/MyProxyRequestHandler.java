/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.collaborator;

import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class MyProxyRequestHandler implements ProxyRequestHandler
{
    private final CollaboratorClient collaboratorClient;

    public MyProxyRequestHandler(CollaboratorClient collaboratorClient)
    {
        this.collaboratorClient = collaboratorClient;
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest)
    {
        String payload = collaboratorClient.generatePayload().toString();

        HttpRequest newRequest = interceptedRequest.withParameter(HttpParameter.urlParameter("host", payload)); // Test this on: https://portswigger-labs.net/ssrf-dns.php

        return ProxyRequestReceivedAction.continueWith(newRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest)
    {
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}
