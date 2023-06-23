/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.collaborator;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.Interaction;

import java.util.List;

import static java.lang.String.format;

public class InteractionLogger
{
    private final MontoyaApi api;

    public InteractionLogger(MontoyaApi api)
    {
        this.api = api;
    }

    public void logInteractions(List<Interaction> allInteractions)
    {
        api.logging().logToOutput(allInteractions.size() + " unread interactions.");

        for (Interaction allInteraction : allInteractions)
        {
            logInteraction(allInteraction);
        }
    }

    public void logInteraction(Interaction interaction)
    {
        api.logging().logToOutput(
                format(
                        """
                        Interaction type: %s
                        Interaction ID: %s
                        """,
                        interaction.type().name(),
                        interaction.id()
                )
        );
    }
}
