package org.bilanzius.commandController;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.*;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;

public class CommandController {

    UserDatabaseService userService;
    TransactionService transactionService;
    CategoryService categoryService;
    private final Map<Commands, Command> commandMap;
    private final Localization localization = Localization.getInstance();

    public CommandController(User user, UserDatabaseService userService,
                             CategoryService categoryService, TransactionService transactionService) {

        this.userService = userService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommand(user));
        commandMap.put(Commands.HELP, new HelpCommand());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommand());
        commandMap.put(Commands.DEPOSIT, new DepositCommand(user));
        commandMap.put(Commands.WITHDRAW, new WithdrawCommand(user));
        commandMap.put(Commands.CONVERT, new ConvertCommand(user));

        // Sprachbefehle
        commandMap.put(Commands.GETLANGUAGES, new GetLanguagesCommand());
        commandMap.put(Commands.SETLANGUAGE, new SetLanguageCommand());

        // Kategoriebefehle
        commandMap.put(Commands.CREATECATEGORY, new CreateCategoryCommand(user, this.categoryService));
        commandMap.put(Commands.GETCATEGORIES, new GetCategoryCommand(user, this.categoryService));
        commandMap.put(Commands.DELETECATEGORY, new DeleteCategoryCommand(user, this.categoryService));

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