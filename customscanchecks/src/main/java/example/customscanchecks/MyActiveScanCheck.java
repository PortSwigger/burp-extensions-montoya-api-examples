/*
 * Copyright (c) 2025. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customscanchecks;

import burp.api.montoya.core.Marker;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ConsolidationAction;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import burp.api.montoya.scanner.scancheck.ActiveScanCheck;

import java.util.List;

import static burp.api.montoya.core.ByteArray.byteArray;
import static burp.api.montoya.scanner.AuditResult.auditResult;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_BOTH;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_EXISTING;
import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;
import static example.customscanchecks.Utilities.getResponseHighlights;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class MyActiveScanCheck implements ActiveScanCheck
{
    private static final String INJ_TEST = "|";
    private static final String INJ_ERROR = "Unexpected pipe";

    @Override
    public String checkName()
    {
        return "Pipe injection";
    }

    @Override
    public AuditResult doCheck(HttpRequestResponse httpRequestResponse, AuditInsertionPoint auditInsertionPoint, Http http)
    {
        HttpRequest checkRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(INJ_TEST)).withService(httpRequestResponse.httpService());

        HttpRequestResponse checkRequestResponse = http.sendRequest(checkRequest);

        List<Marker> responseHighlights = getResponseHighlights(checkRequestResponse, INJ_ERROR);

        List<AuditIssue> auditIssueList = responseHighlights.isEmpty() ? emptyList() : singletonList(
                auditIssue(
                        "Pipe injection",
                        "Submitting a pipe character returned the string: " + INJ_ERROR,
                        null,
                        httpRequestResponse.request().url(),
                        AuditIssueSeverity.HIGH,
                        AuditIssueConfidence.CERTAIN,
                        null,
                        null,
                        AuditIssueSeverity.HIGH,
                        checkRequestResponse.withResponseMarkers(responseHighlights)
                )
        );

        return auditResult(auditIssueList);
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue existingIssue, AuditIssue newIssue)
    {
        return existingIssue.name().equals(newIssue.name()) ? KEEP_EXISTING : KEEP_BOTH;
    }
}
