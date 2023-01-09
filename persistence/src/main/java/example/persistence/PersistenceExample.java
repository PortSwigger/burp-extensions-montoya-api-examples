/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.persistence;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.PersistedObject;

//Burp will auto-detect and load any class that extends BurpExtension.
public class PersistenceExample implements BurpExtension {

    static final String STARTUP_COUNT_KEY = "Startup Count";

    @Override
    public void initialize(MontoyaApi api) {
        //Extension data will use the extension name to save/load for that specific extension
        api.extension().setName("Persistence example extension");

        PersistedObject myExtensionData = api.persistence().extensionData();

        //Retrieve an integer value from the project file
        Integer startupCount = myExtensionData.getInteger(STARTUP_COUNT_KEY);

        //If a value is null, it does not exist in the project file.
        //We could also check if the key exists
        //e.g. myExtensionData.integerKeys().contains(STARTUP_COUNT_KEY)
        if (startupCount == null) {
            startupCount = 0;
        }

        //Set an integer value in the project file
        myExtensionData.setInteger(STARTUP_COUNT_KEY, startupCount + 1);

        //Retrieve the updated value and create a info event.
        api.logging().raiseInfoEvent("Startup count is: " + myExtensionData.getInteger(STARTUP_COUNT_KEY));

        advancedExamples(api);
    }

    private static void advancedExamples(MontoyaApi api) {
        //Advanced example saving and loading requests.
        SavingLoadingRequests savingLoadingRequests = new SavingLoadingRequests(api);
        savingLoadingRequests.runExample();

        //More advanced example working with Lists of HttpRequestResponse.
        RequestResponseLogging requestLogging = new RequestResponseLogging(api);
        requestLogging.runExample();
    }
}
