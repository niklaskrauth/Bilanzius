package org.bilanzius.services.commands;

import org.bilanzius.services.Command;

public class ExitCommand implements Command {

    @Override
    public String execute(String[] arguments) {
        System.out.println("Goodbye User");
        System.exit(0);
        return "";
    }
}