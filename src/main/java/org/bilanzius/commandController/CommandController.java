package org.bilanzius.commandController;

import org.bilanzius.services.CommandService;
import org.bilanzius.services.commands.bilanziusCommand.BilanziusCommandService;
import org.bilanzius.services.commands.helpCommand.HelpCommandService;
import org.bilanzius.services.commands.exitCommand.ExitCommandService;

import java.util.HashMap;
import java.util.Map;

public class CommandController {

    private final Map<Commands, CommandService> commandMap;

    public CommandController() {

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommandService());
        commandMap.put(Commands.HELP, new HelpCommandService());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommandService());

        //Hier werden die einzelnen Befehle über das Enum auf die Klassen gemappt
    }

    public String handleInput(String input) {

        String[] parts = input.split(" ", 2);
        String commandStr = parts[0];
        String[] arguments = parts.length > 1 ? parts[1].split(" ") : new String[0];

        Commands command = Commands.fromString(commandStr);
        CommandService commandService = commandMap.get(command);

        if (commandService != null) {
            return commandService.execute(arguments);
        }

        return "Unknown command :( . Type /help for a list of commands.";
    }
}