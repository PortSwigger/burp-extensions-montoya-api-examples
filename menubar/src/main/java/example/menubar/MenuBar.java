/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.menubar;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.menu.BasicMenuItem;
import burp.api.montoya.ui.menu.Menu;
import burp.api.montoya.ui.menu.MenuItem;

public class MenuBar implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi api)
    {
        api.extension().setName("Add menu bar");
        api.logging().logToOutput("Extension has been loaded.");

        BasicMenuItem alertEventItem = BasicMenuItem.basicMenuItem("Raise critical alert").withAction(() -> api.logging().raiseCriticalEvent("Alert from extension"));

        BasicMenuItem basicMenuItem = MenuItem.basicMenuItem("Unload extension");
        MenuItem unloadExtensionItem = basicMenuItem.withAction(() -> api.extension().unload());

        Menu menu = Menu.menu("Menu bar").withMenuItems(alertEventItem, unloadExtensionItem);

        api.userInterface().menuBar().registerMenu(menu);

        api.extension().registerUnloadingHandler(new MyExtensionUnloadingHandler(api));
    }
}
