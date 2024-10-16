package org.bilanzius.commandController;

import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BilanziusCommand;
import org.bilanzius.services.commands.HelpCommand;
import org.bilanzius.services.commands.ExitCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandController {

    private final Map<Commands, Command> commandMap;

    public CommandController() {

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommand());
        commandMap.put(Commands.HELP, new HelpCommand());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommand());

        //Hier werden die einzelnen Befehle über das Enum auf die Klassen gemappt
    }

    public String handleInput(String input) {

        String[] parts = input.split(" ", 2);
        String commandStr = parts[0];
        String[] arguments = parts.length > 1 ? parts[1].split(" ") : new String[0];

        Commands command = Commands.fromString(commandStr);
        Command commandService = commandMap.get(command);

        if (commandService != null) {
            return commandService.execute(arguments);
        }

        return "Unknown command :( . Type /help for a list of commands.";
    }
}