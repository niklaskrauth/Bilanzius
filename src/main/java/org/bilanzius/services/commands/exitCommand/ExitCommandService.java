package org.bilanzius.services.commands.exitCommand;

import org.bilanzius.services.CommandService;

public class ExitCommandService implements CommandService {

    @Override
    public String execute(String[] arguments) {
        System.out.println("Goodbye User");
        System.exit(0);
        return "";
    }
}