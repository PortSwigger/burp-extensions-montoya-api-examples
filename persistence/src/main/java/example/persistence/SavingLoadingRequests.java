package example.persistence;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.persistence.PersistedObject;

import static burp.api.montoya.http.message.requests.HttpRequest.httpRequest;
import static burp.api.montoya.http.message.requests.HttpRequest.httpRequestFromUrl;
import static example.persistence.PersistenceExample.STARTUP_COUNT_KEY;

public class SavingLoadingRequests {
    public static final String SIMPLE_REQUEST_KEY = "simpleRequest";
    public static final String REQUEST_WITH_HEADERS_KEY = "requestWithHeaders";
    public static final String REQUEST_FROM_URL_KEY = "requestFromUrl";
    private final MontoyaApi api;
    private final PersistedObject myExtensionData;

    public SavingLoadingRequests(MontoyaApi api) {
        this.api = api;
        this.myExtensionData = api.persistence().extensionData();
    }

    public void runExample() {
        //Check if we have example requests saved
        if (!checkForRequests()) {
            api.logging().raiseInfoEvent("No Requests saved, creating requests");
            createAndSaveExampleRequests();
        }

        sendExampleRequestsToRepeaterWithStartupCount();
    }

    private boolean checkForRequests() {
        // We can get a list of keys for each type of data type saved.
        return myExtensionData.httpRequestKeys().contains(SIMPLE_REQUEST_KEY) &&
                myExtensionData.httpRequestKeys().contains(REQUEST_WITH_HEADERS_KEY) &&
                myExtensionData.httpRequestKeys().contains(REQUEST_FROM_URL_KEY);
    }

    //Build example requests and save them to their own key
    private void createAndSaveExampleRequests() {
        HttpRequest simpleRequest = httpRequest("GET / HTTP1.0\r\n\r\n");
        HttpRequest requestWithHeaders = httpRequest("GET / HTTP1.1\r\nHost: localhost\r\nMyHeader: Example\r\n\r\n");
        HttpRequest requestFromUrl = httpRequestFromUrl("http://localhost");

        //Save each request to its own key
        myExtensionData.setHttpRequest(SIMPLE_REQUEST_KEY, simpleRequest);
        myExtensionData.setHttpRequest(REQUEST_WITH_HEADERS_KEY, requestWithHeaders);
        myExtensionData.setHttpRequest(REQUEST_FROM_URL_KEY, requestFromUrl);
    }

    //Add our requests to repeater with startup count in the tab name
    private void sendExampleRequestsToRepeaterWithStartupCount() {
        HttpRequest simpleRequest = myExtensionData.getHttpRequest(SIMPLE_REQUEST_KEY);
        HttpRequest requestWithHeaders = myExtensionData.getHttpRequest(REQUEST_WITH_HEADERS_KEY);
        HttpRequest requestFromUrl = myExtensionData.getHttpRequest(REQUEST_FROM_URL_KEY);

        Integer startupCount = myExtensionData.getInteger(STARTUP_COUNT_KEY);

        api.repeater().sendToRepeater(simpleRequest, "Simple Request " + startupCount);
        api.repeater().sendToRepeater(requestWithHeaders, "Request With Headers " + startupCount);
        api.repeater().sendToRepeater(requestFromUrl, "Request From Url " + startupCount);
    }
}
