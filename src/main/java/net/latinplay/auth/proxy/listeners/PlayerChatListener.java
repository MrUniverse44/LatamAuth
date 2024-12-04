package net.latinplay.auth.proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerChatListener implements Listener {

    @EventHandler(priority=-64)
    public void onChat(ChatEvent e) {

        if(e.isCancelled()) {

            return;
        }

        if (!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }
    }

}
