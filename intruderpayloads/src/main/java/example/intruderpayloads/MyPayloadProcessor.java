/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.intruderpayloads;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.intruder.PayloadData;
import burp.api.montoya.intruder.PayloadProcessingResult;
import burp.api.montoya.intruder.PayloadProcessor;
import burp.api.montoya.utilities.Base64Utils;
import burp.api.montoya.utilities.URLUtils;

import static burp.api.montoya.core.ByteArray.byteArray;
import static burp.api.montoya.intruder.PayloadProcessingResult.usePayload;

public class MyPayloadProcessor implements PayloadProcessor
{
    public static final String INPUT_PREFIX = "input=";
    private final MontoyaApi api;

    public MyPayloadProcessor(MontoyaApi api)
    {
        this.api = api;
    }

    @Override
    public String displayName()
    {
        return "Serialized input wrapper";
    }

    @Override
    public PayloadProcessingResult processPayload(PayloadData payloadData)
    {
        Base64Utils base64Utils = api.utilities().base64Utils();
        URLUtils urlUtils = api.utilities().urlUtils();

        // Decode the base value
        String dataParameter = base64Utils.decode(urlUtils.decode(payloadData.originalPayload())).toString();

        // Parse the location of the input string in the decoded data
        String prefix = findPrefix(dataParameter);
        if (prefix == null)
        {
            return usePayload(payloadData.currentPayload());
        }

        String suffix = findSuffix(dataParameter);

        // Rebuild serialized data with the new payload
        String rebuiltDataParameter = prefix + payloadData.currentPayload() + suffix;

        return usePayload(byteArray(rebuiltDataParameter));
    }

    private String findPrefix(String dataParameter)
    {
        int start = dataParameter.indexOf(INPUT_PREFIX);

        if (start == -1)
        {
            return null;
        }

        start += INPUT_PREFIX.length();

        return dataParameter.substring(0, start);
    }

    private String findSuffix(String dataParameter)
    {
        int start = dataParameter.indexOf(INPUT_PREFIX);

        int end = dataParameter.indexOf("&", start);

        if (end == -1)
        {
            end = dataParameter.length();
        }

        return dataParameter.substring(end);
    }
}
