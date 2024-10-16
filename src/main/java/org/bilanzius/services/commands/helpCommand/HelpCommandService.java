package org.bilanzius.services.commands.helpCommand;
import org.bilanzius.services.CommandService;

import static org.bilanzius.commandController.Commands.getAllCommands;

public class HelpCommandService implements CommandService {

    @Override
    public String execute(String[] arguments) {
        return getAllCommands();
    }
}