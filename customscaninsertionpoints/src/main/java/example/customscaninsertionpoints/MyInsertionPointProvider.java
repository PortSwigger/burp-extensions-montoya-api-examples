/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customscaninsertionpoints;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPointProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

class MyInsertionPointProvider implements AuditInsertionPointProvider
{
    private final MontoyaApi api;

    MyInsertionPointProvider(MontoyaApi api)
    {
        this.api = api;
    }

    @Override
    public List<AuditInsertionPoint> provideInsertionPoints(HttpRequestResponse baseHttpRequestResponse)
    {
        List<ParsedHttpParameter> parameters = baseHttpRequestResponse.request().parameters();

        return parameters.stream()
                .filter(p -> p.name().equals("data"))
                .map(p -> new MyAuditInsertionPoint(api, baseHttpRequestResponse, p))
                .collect(toList());
    }
}
