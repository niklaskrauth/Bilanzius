package org.bilanzius.commandController;

import org.bilanzius.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.*;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;

public class CommandController {

    private User user;
    private final Map<Commands, Command> commandMap;
    private final Localization localization = Localization.getInstance();

    public CommandController(User user) {

        this.user = user;

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommand(this.user));
        commandMap.put(Commands.HELP, new HelpCommand());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommand());
        commandMap.put(Commands.DEPOSIT, new DepositCommand(this.user));
        commandMap.put(Commands.WITHDRAW, new WithdrawCommand(this.user));
        commandMap.put(Commands.CONVERT, new ConvertCommand(this.user));

        // Sprachbefehle
        commandMap.put(Commands.GETLANGUAGES, new GetLanguagesCommand());
        commandMap.put(Commands.SETLANGUAGE, new SetLanguageCommand());

        // Kategoriebefehle
        commandMap.put(Commands.CREATECATEGORY, new CreateCategoryCommand(this.user));
        commandMap.put(Commands.GETCATEGORIES, new GetCategoryCommand(this.user));

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

        return localization.getMessage("unknown_command");
    }
}