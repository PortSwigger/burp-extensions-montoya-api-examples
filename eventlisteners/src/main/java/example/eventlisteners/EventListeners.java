/*
 * Copyright (c) 2022. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.eventlisteners;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.Extension;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.Proxy;
import burp.api.montoya.proxy.http.*;
import burp.api.montoya.scanner.Scanner;
import burp.api.montoya.scanner.audit.AuditIssueHandler;
import burp.api.montoya.scanner.audit.issues.AuditIssue;


//Burp will auto-detect and load any class that extends BurpExtension.
public class EventListeners implements BurpExtension {
    private Logging logging;

    @Override
    public void initialize(MontoyaApi api) {
        logging = api.logging();

        Http http = api.http();
        Proxy proxy = api.proxy();
        Extension extension = api.extension();
        Scanner scanner = api.scanner();

        // set extension name
        extension.setName("Event listeners");

        // register a new HTTP handler
        http.registerHttpHandler(new MyHttpHandler());

        // register a new Proxy handler
        proxy.registerRequestHandler(new MyProxyHttpRequestHandler());
        proxy.registerResponseHandler(new MyProxyHttpResponseHandler());

        // register a new Audit Issue handler
        scanner.registerAuditIssueHandler(new MyAuditIssueListenerHandler());

        // register a new extension unload handler
        extension.registerUnloadingHandler(new MyExtensionUnloadHandler());
    }

    private class MyHttpHandler implements HttpHandler {
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

    private class MyProxyHttpRequestHandler implements ProxyRequestHandler {
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

    private class MyProxyHttpResponseHandler implements ProxyResponseHandler {
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

    private class MyAuditIssueListenerHandler implements AuditIssueHandler {
        @Override
        public void handleNewAuditIssue(AuditIssue auditIssue) {
            logging.logToOutput("New scan issue: " + auditIssue.name());
        }
    }

    private class MyExtensionUnloadHandler implements ExtensionUnloadingHandler {
        @Override
        public void extensionUnloaded() {
            logging.logToOutput("Extension was unloaded.");
        }
    }
}