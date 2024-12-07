package net.latinplay.auth.proxy.commands.object;

import me.blueslime.bungeemeteor.commands.InjectedCommand;
import me.blueslime.bungeemeteor.libs.utilitiesapi.commands.sender.Sender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class ProxyCommand extends InjectedCommand {

    public ProxyCommand(String command) {
        super(command);
    }

    /**
     * Execute an async command
     * @param sender of the command
     * @param args from the command
     */
    public abstract void execute(Sender sender, String[] args);

    @Override
    public void executeCommand(Sender sender, String[] strings) {
        CompletableFuture.supplyAsync(
            () -> {
                execute(sender, strings);
                return null;
            },
            fetch(ExecutorService.class)
        );
    }
}
