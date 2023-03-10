/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customsessiontokens;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

//Burp will auto-detect and load any class that extends BurpExtension.
public class CustomSessionTokens implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi api)
    {
        api.extension().setName("Session token example");

        api.http().registerSessionHandlingAction(new MySessionHandlingAction());
    }
}
