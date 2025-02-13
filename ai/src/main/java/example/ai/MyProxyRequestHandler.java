/*
 * Copyright (c) 2025. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.PromptException;
import burp.api.montoya.ai.chat.PromptResponse;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

import java.util.concurrent.ExecutorService;

import static burp.api.montoya.core.HighlightColor.GREEN;
import static java.util.Locale.US;

class MyProxyRequestHandler implements ProxyRequestHandler
{
    private final Ai ai;
    private final Logging logging;
    private final MyPromptMessage promptMessageHandler;
    private final ExecutorService executorService;

    public MyProxyRequestHandler(Ai ai, Logging logging, ExecutorService executorService, MyPromptMessage promptMessageHandler)
    {
        this.ai = ai;
        this.logging = logging;
        this.promptMessageHandler = promptMessageHandler;
        this.executorService = executorService;
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest)
    {
        if (ai.isEnabled() && interceptedRequest.isInScope())
        {
            // AI requests are slow, so should be run on a separate thread.
            executorService.submit(() -> processRequest(interceptedRequest));
        }

        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest)
    {
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }

    private void processRequest(InterceptedRequest request)
    {
        try
        {
            PromptResponse promptResponse = ai.prompt().execute(promptMessageHandler.build(request.toString()));

            if (promptResponse.content().toLowerCase(US).contains("yes"))
            {
                request.annotations().setHighlightColor(GREEN);
            }
        } catch (PromptException e)
        {
            if (e.getMessage().contains("Not enough credits"))
            {
                logging.logToOutput("Please increase your credit balance.");
            } else
            {
                logging.logToError("Issue executing prompt", e);
            }
        }
    }
}