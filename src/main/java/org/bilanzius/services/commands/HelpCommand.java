package org.bilanzius.services.commands;
import org.bilanzius.services.Command;

import static org.bilanzius.commandController.Commands.getAllCommands;

public class HelpCommand implements Command {

    @Override
    public String execute(String[] arguments) {
        return getAllCommands();
    }
}