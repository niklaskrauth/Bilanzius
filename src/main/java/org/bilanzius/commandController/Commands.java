package org.bilanzius.commandController;

import org.bilanzius.services.commands.*;
import org.bilanzius.utils.Localization;

import java.util.Arrays;

public enum Commands {
    EXIT("/exit", Localization.getInstance().getMessage("exit_short_description"), null),
    HELP("/help", Localization.getInstance().getMessage("help_short_description"), null),
    BILANZIUS("/bilanzius", Localization.getInstance().getMessage("bilanzius_short_description"),
            BilanziusCommandArguments.getAllArguments()),
    DEPOSIT("/deposit", Localization.getInstance().getMessage("deposit_short_description"),
            DepositCommandArguments.getAllArguments()),
    WITHDRAW("/withdraw", Localization.getInstance().getMessage("withdraw_short_description"),
            WithdrawCommandArgument.getAllArguments()),
    CONVERT("/convert", Localization.getInstance().getMessage("convert_short_description"),
            ConvertCommandArguments.getAllArguments()),
    GETLANGUAGES("/getLanguages", Localization.getInstance().getMessage("get_languages_description"), null),
    SETLANGUAGE("/setLanguage", Localization.getInstance().getMessage("set_language_description"), null),
    CREATECATEGORY("/createCategory", Localization.getInstance().getMessage("create_category_short_description"),
            CreateCategoryCommandArguments.getAllArguments()),
    GETCATEGORIES("/getCategories", Localization.getInstance().getMessage("get_categories_short_description"),
            GetCategoryCommandArguments.getAllArguments());

    // Hier werden die einzelnen Befehle hinzugefügt

    private final String command;
    private final String description;
    private final String arguments;

    Commands(String command, String description, String arguments) {
        this.command = command;
        this.description = description;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getArguments() {
        return arguments;
    }

    public static String getAllCommands() {

        return Arrays.stream(Commands.values()).map(
                c -> c.getCommand()
                        + " - " +
                        c.getDescription()
                        +
                        (
                                c.getArguments()
                                        != null ?
                                        " | " + (c.getArguments())
                                        : ""
                        )
                ).reduce(
                        (a, b) -> a + "\n" + b
                ).orElse("");
    }

    public static Commands fromString(String command) {
        for (Commands c : Commands.values()) {
            if (c.command.equals(command)) {
                return c;
            }
        }
        return null;
    }

}