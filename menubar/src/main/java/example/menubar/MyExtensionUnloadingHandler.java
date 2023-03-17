/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.menubar;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

public class MyExtensionUnloadingHandler implements ExtensionUnloadingHandler
{
    private final MontoyaApi api;

    public MyExtensionUnloadingHandler(MontoyaApi api)
    {
        this.api = api;
    }

    @Override
    public void extensionUnloaded()
    {
        api.logging().logToOutput("Extension has been unloaded.");
    }
}
