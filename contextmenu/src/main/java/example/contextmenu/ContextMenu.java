/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.contextmenu;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class ContextMenu implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi api)
    {
        api.extension().setName("Context menu extension");

        api.userInterface().registerContextMenuItemsProvider(new MyContextMenuItemsProvider(api));
    }
}
