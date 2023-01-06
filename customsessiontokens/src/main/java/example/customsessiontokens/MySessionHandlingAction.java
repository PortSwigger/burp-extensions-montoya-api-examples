/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customsessiontokens;

import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.sessions.ActionResult;
import burp.api.montoya.http.sessions.SessionHandlingAction;
import burp.api.montoya.http.sessions.SessionHandlingActionData;

import java.util.List;

import static burp.api.montoya.http.sessions.ActionResult.actionResult;

public class MySessionHandlingAction implements SessionHandlingAction
{
    @Override
    public String name()
    {
        return "Use session token from macro";
    }

    @Override
    public ActionResult performAction(SessionHandlingActionData actionData)
    {
        ActionResult result = actionResult(actionData.request(), actionData.annotations());

        List<HttpRequestResponse> macroRequestResponseList = actionData.macroRequestResponses();

        if (macroRequestResponseList.isEmpty())
        {
            return result;
        }

        // Extract the response headers
        List<HttpHeader> headers = macroRequestResponseList.get(macroRequestResponseList.size()-1).response().headers();

        // Find session header
        HttpHeader sessionHeader = findSessionHeader(headers);

        // If we failed to find a session token, stop doing work
        if (sessionHeader == null)
        {
            return result;
        }

        // Create an HTTP request with updated session header
        HttpRequest updatedRequest = actionData.request().withUpdatedHeader(sessionHeader);

        return actionResult(updatedRequest, actionData.annotations());
    }

    private HttpHeader findSessionHeader(List<HttpHeader> headers)
    {
        HttpHeader sessionHeader = null;

        for(HttpHeader header : headers)
        {
            // Skip any header that isn't an "X-Custom-Session-Id"
            if (!header.name().startsWith(CustomSessionTokens.SESSION_ID_KEY))
            {
                continue;
            }

            // Grab the session token
            sessionHeader = header;
        }

        return sessionHeader;
    }
}
