package org.bilanzius.services.commands;

import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

public class ExitCommand implements Command {
    private User user;

    public ExitCommand(User user) {
        this.user = user;
    }

    @Override
    public String execute(String[] arguments) {
        System.out.println(Localization.getInstance().getMessage("farewell", user.getUsername()));
        System.exit(0);
        return "";
    }
}