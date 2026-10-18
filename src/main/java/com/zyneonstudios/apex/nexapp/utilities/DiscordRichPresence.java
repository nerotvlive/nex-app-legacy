package com.zyneonstudios.apex.nexapp.utilities;

import com.zyneonstudios.apex.nexapp.main.NEXApplication;

public class DiscordRichPresence {

    private static club.minnced.discord.rpc.DiscordRPC discordRPC;
    private static club.minnced.discord.rpc.DiscordEventHandlers handlers;
    private static club.minnced.discord.rpc.DiscordRichPresence presence;
    private static Thread discordThread;

    private static void init() {
        try {
            if(discordRPC == null) {
                discordRPC = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
            }
            if(handlers == null) {
                handlers = new club.minnced.discord.rpc.DiscordEventHandlers();
            }
            if(presence == null) {
                presence = new club.minnced.discord.rpc.DiscordRichPresence();
            }
        } catch (Exception e) {
            NEXApplication.getLogger().deb("Failed to initialize Discord Rich Presence: " + e.getMessage());
        }
    }

    public static void startRPC() {
        try {
            init();
            String applicationId = "1407342863204880639";
            handlers.ready = (user) -> NEXApplication.getLogger().deb("Successfully started Discord Rich Presence!");
            discordRPC.Discord_Initialize(applicationId, handlers, true, "");
            presence = new club.minnced.discord.rpc.DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.details = "Launching application...";
            discordRPC.Discord_UpdatePresence(presence);
            // in a worker thread
            discordThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    discordRPC.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");
            discordThread.start();
        } catch (Exception e) {
            NEXApplication.getLogger().err("Failed to start Discord Rich Presence: "+e.getMessage());
        }
    }

    public static void stopRPC() {
        try {
            discordRPC.Discord_Shutdown();
            discordThread.interrupt();
            discordThread = null;
        } catch (Exception ignored) {}
    }

    public static void setDetails(String details) {
        try {
            init();
            if (presence != null && discordThread != null) {
                presence.details = details;
                discordRPC.Discord_UpdatePresence(presence);
            }
        } catch (Exception e) {
            NEXApplication.getLogger().err("Failed to update Discord Rich Presence: "+e.getMessage());
        }
    }
}