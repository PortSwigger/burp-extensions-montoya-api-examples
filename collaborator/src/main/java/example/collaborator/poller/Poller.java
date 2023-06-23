/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.collaborator.poller;

import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.Interaction;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Poller
{
    private final CollaboratorClient collaboratorClient;
    private final Duration pollInterval;
    private final List<InteractionHandler> interactionHandlers;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture<?> schedule;

    public Poller(CollaboratorClient collaboratorClient, Duration pollInterval)
    {
        this.collaboratorClient = collaboratorClient;
        this.pollInterval = pollInterval;
        this.interactionHandlers = new LinkedList<>();
    }

    public void registerInteractionHandler(InteractionHandler interactionHandler)
    {
        interactionHandlers.add(interactionHandler);
    }

    public void start()
    {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        schedule = scheduledThreadPoolExecutor.scheduleAtFixedRate(new PollingRunnable(), 0, pollInterval.getSeconds(), TimeUnit.SECONDS);
    }

    public void shutdown()
    {
        schedule.cancel(true);
        scheduledThreadPoolExecutor.shutdown();
    }

    private class PollingRunnable implements Runnable
    {
        @Override
        public void run()
        {
            List<Interaction> interactionList = collaboratorClient.getAllInteractions();

            for (Interaction interaction : interactionList)
            {
                for (InteractionHandler interactionHandler : interactionHandlers)
                {
                    interactionHandler.handleInteraction(interaction);
                }
            }
        }
    }
}
