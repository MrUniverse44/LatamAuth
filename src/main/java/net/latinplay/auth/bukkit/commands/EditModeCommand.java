package net.latinplay.auth.bukkit.commands;

import me.blueslime.bukkitmeteor.commands.InjectedCommand;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.commands.sender.Sender;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EditModeCommand extends InjectedCommand {

    private final Set<UUID> editors = new HashSet<>();

    @Override
    public void executeCommand(Sender sender, String label, String[] args) {
        if (!sender.hasPermission("latamauth.admin") || !sender.isPlayer()) {
            return;
        }

        UUID uuid = sender.toPlayer().getUniqueId();

        if (editors.contains(sender.toPlayer().getUniqueId())) {
            editors.remove(uuid);
            sender.send("&aEditMode: &cNO");
        } else {
            editors.add(uuid);
            sender.send("&aEditMode: &bYES");
        }
    }

    public Set<UUID> getEditors() {
        return editors;
    }
}
