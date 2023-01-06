/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customlogger;

import burp.api.montoya.http.handler.*;

public class MyHttpHandler implements HttpHandler
{
    private final MyTableModel tableModel;

    public MyHttpHandler(MyTableModel tableModel)
    {

        this.tableModel = tableModel;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent)
    {
        return RequestToBeSentAction.continueWith(requestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived)
    {
        tableModel.add(responseReceived);
        return ResponseReceivedAction.continueWith(responseReceived);
    }
}
