/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.collaborator;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.SecretKey;
import burp.api.montoya.persistence.PersistedObject;
import example.collaborator.poller.Poller;

import java.time.Duration;

public class CollaboratorExample implements BurpExtension
{
    private MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api)
    {
        this.api = api;

        api.extension().setName("Collaborator example extension");

        CollaboratorClient collaboratorClient = createCollaboratorClient(api.persistence().extensionData());

        // Log any stored interactions.
        InteractionLogger interactionLogger = new InteractionLogger(api);
        interactionLogger.logInteractions(collaboratorClient.getAllInteractions());

        api.proxy().registerRequestHandler(new MyProxyRequestHandler(collaboratorClient));

        // Periodically poll the CollaboratorClient to retrieve any new interactions.
        Poller collaboratorPoller = new Poller(collaboratorClient, Duration.ofSeconds(10));
        collaboratorPoller.registerInteractionHandler(new MyInteractionHandler(api, interactionLogger));
        collaboratorPoller.start();

        api.extension().registerUnloadingHandler(() ->
        {
            // Stop polling the CollaboratorClient.
            collaboratorPoller.shutdown();

            api.logging().logToOutput("Extension unloading...");
        });
    }

    private CollaboratorClient createCollaboratorClient(PersistedObject persistedData)
    {
        CollaboratorClient collaboratorClient;

        String existingCollaboratorKey = persistedData.getString("persisted_collaborator");

        if (existingCollaboratorKey != null)
        {
            api.logging().logToOutput("Creating Collaborator client from key.");
            collaboratorClient = api.collaborator().restoreClient(SecretKey.secretKey(existingCollaboratorKey));
        }
        else
        {
            api.logging().logToOutput("No previously found Collaborator client. Creating new client...");
            collaboratorClient = api.collaborator().createClient();

            // Save the secret key of the CollaboratorClient so that you can retrieve it later.
            api.logging().logToOutput("Saving Collaborator secret key.");
            persistedData.setString("persisted_collaborator", collaboratorClient.getSecretKey().toString());
        }

        return collaboratorClient;
    }
}
