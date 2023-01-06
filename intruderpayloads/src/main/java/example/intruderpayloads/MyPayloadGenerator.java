/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.intruderpayloads;

import burp.api.montoya.intruder.GeneratedPayload;
import burp.api.montoya.intruder.IntruderInsertionPoint;
import burp.api.montoya.intruder.PayloadGenerator;

import java.util.List;

public class MyPayloadGenerator implements PayloadGenerator
{
    private static final List<String> PAYLOADS = List.of("|", "<script>alert(1)</script>");
    private int payloadIndex;

    @Override
    public GeneratedPayload generatePayloadFor(IntruderInsertionPoint insertionPoint)
    {
        payloadIndex++;

        if (payloadIndex > PAYLOADS.size())
        {
            return GeneratedPayload.end();
        }

        String payload = PAYLOADS.get(payloadIndex);

        return GeneratedPayload.payload(payload);
    }
}
