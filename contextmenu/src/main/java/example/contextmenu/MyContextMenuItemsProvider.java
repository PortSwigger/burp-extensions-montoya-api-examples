/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.contextmenu;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyContextMenuItemsProvider implements ContextMenuItemsProvider
{

    private final MontoyaApi api;
    private final JMenuItem retrieveRequestItem;
    private final JMenuItem retrieveResponseItem;

    public MyContextMenuItemsProvider(MontoyaApi api)
    {
        this.api = api;

        retrieveRequestItem = new JMenuItem("Print request");
        retrieveResponseItem = new JMenuItem("Print response");
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event)
    {
        if (event.isFromTool(ToolType.PROXY, ToolType.TARGET, ToolType.LOGGER))
        {
            List<Component> menuItemList = new ArrayList<>();

            HttpRequestResponse requestResponse = event.messageEditorRequestResponse().isPresent() ? event.messageEditorRequestResponse().get().requestResponse() : event.selectedRequestResponses().get(0);

            retrieveRequestItem.addActionListener(l -> api.logging().logToOutput("Request is:\r\n" + requestResponse.request().toString()));
            menuItemList.add(retrieveRequestItem);

            if (requestResponse.response() != null)
            {
                retrieveResponseItem.addActionListener(l -> api.logging().logToOutput("Response is:\r\n" + requestResponse.response().toString()));
                menuItemList.add(retrieveResponseItem);
            }

            return menuItemList;
        }

        return null;
    }
}
