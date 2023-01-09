package example.persistence;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.persistence.PersistedList;
import burp.api.montoya.persistence.PersistedObject;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;
import static burp.api.montoya.http.message.HttpRequestResponse.httpRequestResponse;

public class RequestResponseLogging {
    private static final String REQUEST_RESPONSE_LIST_KEY = "last5";

    private final MontoyaApi api;
    private final PersistedObject myExtensionData;

    public RequestResponseLogging(MontoyaApi api) {
        this.api = api;
        this.myExtensionData = api.persistence().extensionData();
    }

    public void runExample() {
        ensurePersistedListIsPresent();

        //Load our request/response list from the project file.
        PersistedList<HttpRequestResponse> myPersistedList = myExtensionData.getHttpRequestResponseList(REQUEST_RESPONSE_LIST_KEY);
        printToOutput(myPersistedList);

        api.http().registerHttpHandler(new HttpHandler() {
            @Override
            public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
                return continueWith(requestToBeSent);
            }

            @Override
            public synchronized ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
                //Keep a list of the last 5
                if (myPersistedList.size() >= 5) {
                    myPersistedList.remove(0);
                }

                //We don't need the body for our log.
                myPersistedList.add(httpRequestResponse(responseReceived.initiatingRequest().withBody(""), responseReceived.withBody("")));

                return continueWith(responseReceived);
            }
        });
    }

    private void printToOutput(PersistedList<HttpRequestResponse> myPersistedList) {
        //Print the loaded list to the output log
        for (HttpRequestResponse httpRequestResponse : myPersistedList) {
            api.logging().logToOutput(httpRequestResponse.request().toString());
            api.logging().logToOutput("\n========================\n");
            api.logging().logToOutput(httpRequestResponse.response().toString());
            api.logging().logToOutput("\n**************************".repeat(2));
        }
    }

    private void ensurePersistedListIsPresent() {
        //Create a persisted request/response list
        if (myExtensionData.getHttpRequestResponseList(REQUEST_RESPONSE_LIST_KEY) == null) {
            //Create a new empty list and save it to our key
            PersistedList<HttpRequestResponse> emptyPersistedList = PersistedList.persistedHttpRequestResponseList();
            myExtensionData.setHttpRequestResponseList(REQUEST_RESPONSE_LIST_KEY, emptyPersistedList);
        }
    }
}
