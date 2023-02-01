/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.trafficredirector;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

//Burp will auto-detect and load any class that extends BurpExtension.
public class TrafficRedirector implements BurpExtension {
    static final String HOST_FROM = "portswigger-labs.net";
    static final String HOST_TO = "ginandjuice.shop";

    @Override
    public void initialize(MontoyaApi api) {
        // set extension name
        api.extension().setName("Traffic redirector");

        // register a new HTTP handler
        api.http().registerHttpHandler(new MyHttpHandler());
    }
}
