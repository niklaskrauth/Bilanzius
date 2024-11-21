package org.bilanzius.commandController;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.bilanzius.BilanziusCommand;
import org.bilanzius.services.commands.convert.ConvertCommand;
import org.bilanzius.services.commands.createCategory.CreateCategoryCommand;
import org.bilanzius.services.commands.deleteCategory.DeleteCategoryCommand;
import org.bilanzius.services.commands.deposit.DepositCommand;
import org.bilanzius.services.commands.exit.ExitCommand;
import org.bilanzius.services.commands.getCategory.GetCategoryCommand;
import org.bilanzius.services.commands.getLanguage.GetLanguagesCommand;
import org.bilanzius.services.commands.help.HelpCommand;
import org.bilanzius.services.commands.setLanguage.SetLanguageCommand;
import org.bilanzius.services.commands.withdraw.WithdrawCommand;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.Map;

public class CommandController {

    UserService userService;
    TransactionService transactionService;
    CategoryService categoryService;
    private final Map<Commands, Command> commandMap;
    private final Localization localization = Localization.getInstance();

    public CommandController(User user, UserService userService,
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