/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customscaninsertionpoints;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.core.Range;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.utilities.Utilities;

import java.util.List;

import static burp.api.montoya.http.message.params.HttpParameter.parameter;

class MyAuditInsertionPoint implements AuditInsertionPoint
{
    private final String insertionPointPrefix;
    private final String insertionPointSuffix;
    private final HttpRequestResponse requestResponse;
    private final ParsedHttpParameter parameter;
    private final String baseValue;
    private final Utilities utilities;

    MyAuditInsertionPoint(MontoyaApi api, HttpRequestResponse baseHttpRequestResponse, ParsedHttpParameter parameter)
    {
        this.requestResponse = baseHttpRequestResponse;
        this.parameter = parameter;
        this.utilities = api.utilities();

        String paramValue = parameter.value();

        // URL and base-64 decode the data
        String urlDecoded = utilities.urlUtils().decode(paramValue);
        ByteArray byteData = utilities.base64Utils().decode(urlDecoded);

        String data = byteData.toString();

        // Parse the location of the input string within the decoded data
        int start = data.indexOf("input=") + 6;
        int end = data.indexOf("&", start);

        if (end == -1)
        {
            end = data.length();
        }

        baseValue = data.substring(start, end);

        insertionPointPrefix = data.substring(0, start);
        insertionPointSuffix = data.substring(end);
    }

    @Override
    public String name()
    {
        return "Base64-wrapped input";
    }

    @Override
    public String baseValue()
    {
        return baseValue;
    }

    @Override
    public HttpRequest buildHttpRequestWithPayload(ByteArray payload)
    {
        // build the raw data using the specified payload
        String input = insertionPointPrefix + payload.toString() + insertionPointSuffix;

        // Base-64 and URL-encode the data
        String updatedParameterValue = utilities.urlUtils().encode(utilities.base64Utils().encodeToString(input));

        HttpParameter updatedParameter = parameter(parameter.name(), updatedParameterValue, parameter.type());

        return requestResponse.request().withUpdatedParameters(updatedParameter);
    }

    @Override
    public List<Range> issueHighlights(ByteArray payload)
    {
        return null;
    }
}
