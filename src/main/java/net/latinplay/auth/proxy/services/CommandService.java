package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.commands.list.admin.AdminCommand;
import net.latinplay.auth.proxy.commands.list.auth.LoginCommand;
import net.latinplay.auth.proxy.commands.list.auth.RegisterCommand;

public class CommandService implements AdvancedModule {

    @Override
    public void initialize() {
        new RegisterCommand("register").register();
        new LoginCommand("login").register();
        new AdminCommand("auth").register();
    }
}
