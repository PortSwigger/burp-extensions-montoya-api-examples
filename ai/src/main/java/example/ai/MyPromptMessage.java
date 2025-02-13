/*
 * Copyright (c) 2025. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.ai;

import burp.api.montoya.ai.chat.Message;

import static burp.api.montoya.ai.chat.Message.systemMessage;
import static burp.api.montoya.ai.chat.Message.userMessage;

public class MyPromptMessage
{
    private final Message systemMessage;

    public MyPromptMessage(String systemPrompt)
    {
        systemMessage = systemMessage(systemPrompt);
    }

    public Message[] build(String userPrompt)
    {
        return new Message[]{systemMessage, userMessage(userPrompt)};
    }
}
