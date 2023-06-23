/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.collaborator.poller;

import burp.api.montoya.collaborator.Interaction;

public interface InteractionHandler
{
    void handleInteraction(Interaction interaction);
}
