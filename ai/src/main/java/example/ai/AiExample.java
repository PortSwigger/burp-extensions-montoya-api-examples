/*
 * Copyright (c) 2025. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.ai;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.EnhancedCapability;
import burp.api.montoya.MontoyaApi;

import java.util.Set;

import static burp.api.montoya.EnhancedCapability.AI_FEATURES;

public class AiExample implements BurpExtension
{
    public static final String SYSTEM_MESSAGE = """
                                                You are an API request analyzer. Your job is to determine whether an HTTP request might be related to authentication.
                                                You must analyze the content and context of the request carefully and respond only with "Yes" if the request is related to authentication, or "No" if it is not.
                                                Do not provide any additional information beyond "Yes" or "No."
                                                """;

    @Override
    public void initialize(MontoyaApi api)
    {
        api.extension().setName("AI extension");

        MyPromptMessage promptMessageHandler = new MyPromptMessage(SYSTEM_MESSAGE);

        api.proxy().registerRequestHandler(new MyProxyRequestHandler(api.ai(), api.logging(), promptMessageHandler));
    }

    @Override
    public Set<EnhancedCapability> enhancedCapabilities()
    {
        return Set.of(AI_FEATURES);
    }
}