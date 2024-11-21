package org.bilanzius.services.commands.exit;

import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

public class ExitCommand implements Command {
    private User user;
    private final Localization localization = Localization.getInstance();

    public ExitCommand(User user) {
        this.user = user;
    }

    @Override
    public String execute(String[] arguments) {
        System.out.println(localization.getMessage("farewell", user.getUsername()));
        System.exit(0);
        return "";
    }
}