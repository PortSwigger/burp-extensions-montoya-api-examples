/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customrequesteditortab;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpRequestEditor;
import burp.api.montoya.utilities.Base64EncodingOptions;
import burp.api.montoya.utilities.Base64Utils;
import burp.api.montoya.utilities.URLUtils;

import java.awt.*;
import java.util.Optional;

import static burp.api.montoya.core.ByteArray.byteArray;

class MyExtensionProvidedHttpRequestEditor implements ExtensionProvidedHttpRequestEditor
{
    private final RawEditor requestEditor;
    private final Base64Utils base64Utils;
    private final URLUtils urlUtils;
    private HttpRequestResponse requestResponse;
    private final MontoyaApi api;

    private ParsedHttpParameter parsedHttpParameter;

    MyExtensionProvidedHttpRequestEditor(MontoyaApi api, EditorCreationContext creationContext)
    {
        this.api = api;
        base64Utils = api.utilities().base64Utils();
        urlUtils = api.utilities().urlUtils();

        if (creationContext.editorMode() == EditorMode.READ_ONLY)
        {
            requestEditor = api.userInterface().createRawEditor(EditorOptions.READ_ONLY);
        }
        else {
            requestEditor = api.userInterface().createRawEditor();
        }
    }

    @Override
    public HttpRequest getRequest()
    {
        HttpRequest request;

        if (requestEditor.isModified())
        {
            // reserialize data
            String base64Encoded = base64Utils.encodeToString(requestEditor.getContents(), Base64EncodingOptions.URL);
            String encodedData = urlUtils.encode(base64Encoded);

            request = requestResponse.request().withUpdatedParameters(HttpParameter.parameter(parsedHttpParameter.name(), encodedData, parsedHttpParameter.type()));
        }
        else
        {
            request = requestResponse.request();
        }

        return request;
    }

    @Override
    public void setRequestResponse(HttpRequestResponse requestResponse)
    {
        this.requestResponse = requestResponse;

        String urlDecoded = urlUtils.decode(parsedHttpParameter.value());

        ByteArray output;

        try
        {
            output = base64Utils.decode(urlDecoded);
        }
        catch (Exception e)
        {
            output = byteArray(urlDecoded);
        }

        this.requestEditor.setContents(output);
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse requestResponse)
    {
        Optional<ParsedHttpParameter> dataParam = requestResponse.request().parameters().stream().filter(p -> p.name().equals("data")).findFirst();

        dataParam.ifPresent(httpParameter -> parsedHttpParameter = httpParameter);

        return dataParam.isPresent();
    }

    @Override
    public String caption()
    {
        return "Serialized input";
    }

    @Override
    public Component uiComponent()
    {
        return requestEditor.uiComponent();
    }

    @Override
    public Selection selectedData()
    {
        return requestEditor.selection().isPresent() ? requestEditor.selection().get() : null;
    }

    @Override
    public boolean isModified()
    {
        return requestEditor.isModified();
    }
}