package org.bilanzius.commandController;

import org.bilanzius.services.commands.bilanzius.BilanziusCommandArguments;
import org.bilanzius.services.commands.convert.ConvertCommandArguments;
import org.bilanzius.services.commands.createCategory.CreateCategoryCommandArguments;
import org.bilanzius.services.commands.deleteCategory.DeleteCategoryCommandArguments;
import org.bilanzius.services.commands.deposit.DepositCommandArguments;
import org.bilanzius.services.commands.getBankAccount.GetBankAccountCommandArguments;
import org.bilanzius.services.commands.getCategory.GetCategoryCommandArguments;
import org.bilanzius.services.commands.withdraw.WithdrawCommandArgument;
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
    LOG("/log", Localization.getInstance().getMessage("logs_short_description"), null),
    CONVERT("/convert", Localization.getInstance().getMessage("convert_short_description"),
            ConvertCommandArguments.getAllArguments()),
    GETLANGUAGES("/getLanguages", Localization.getInstance().getMessage("get_languages_description"), null),
    SETLANGUAGE("/setLanguage", Localization.getInstance().getMessage("set_language_description"), null),
    CREATECATEGORY("/createCategory", Localization.getInstance().getMessage("create_category_short_description"),
            CreateCategoryCommandArguments.getAllArguments()),
    GETCATEGORIES("/getCategory", Localization.getInstance().getMessage("get_category_short_description"),
            GetCategoryCommandArguments.getAllArguments()),
    DELETECATEGORY("/deleteCategory", Localization.getInstance().getMessage("delete_category_short_description"),
            DeleteCategoryCommandArguments.getAllArguments()),
    CREATEBANKACCOUNT("/createBankAccount", Localization.getInstance().getMessage("create_bank_account_short_description"), null),
    GETBANKACCOUNT("/getBankAccount", Localization.getInstance().getMessage("get_bank_account_short_description"),
            GetBankAccountCommandArguments.getAllArguments()),
    DELETEBANKACCOUNT("/deleteBankAccount", Localization.getInstance().getMessage("delete_bank_account_short_description"),
            DeleteCategoryCommandArguments.getAllArguments()),
    RENAMEBANKACCOUNT("/renameBankAccount", Localization.getInstance().getMessage("rename_bank_account_short_description"), null),
    SWITCHBANKACCOUNT("/switchBankAccount", Localization.getInstance().getMessage("switch_bank_account_short_description"), null),
    HISTORY("/history", Localization.getInstance().getMessage("history_short_description"), null),
    REPORT("/report", Localization.getInstance().getMessage("report"), null);


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