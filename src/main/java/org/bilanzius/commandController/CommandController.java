package org.bilanzius.commandController;

import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.*;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.services.commands.bilanzius.BilanziusCommand;
import org.bilanzius.services.commands.convert.ConvertCommand;
import org.bilanzius.services.commands.createBankAccount.CreateBankAccountCommand;
import org.bilanzius.services.commands.createCategory.CreateCategoryCommand;
import org.bilanzius.services.commands.deleteBankAccount.DeleteBankAccountCommand;
import org.bilanzius.services.commands.deleteCategory.DeleteCategoryCommand;
import org.bilanzius.services.commands.deposit.DepositCommand;
import org.bilanzius.services.commands.exit.ExitCommand;
import org.bilanzius.services.commands.getBankAccount.GetBankAccountCommand;
import org.bilanzius.services.commands.getCategory.GetCategoryCommand;
import org.bilanzius.services.commands.getLanguage.GetLanguagesCommand;
import org.bilanzius.services.commands.help.HelpCommand;
import org.bilanzius.services.commands.history.HistoryCommand;
import org.bilanzius.services.commands.setLanguage.SetLanguageCommand;
import org.bilanzius.services.commands.renameBankAccount.RenameBankAccountCommand;
import org.bilanzius.services.commands.switchBankAccount.SwitchBankAccountCommand;
import org.bilanzius.services.commands.withdraw.WithdrawCommand;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandController {
    private final Map<Commands, Command> commandMap;
    private final Localization localization = Localization.getInstance();
    private BankAccount selectedBankAccount;

    public CommandController(User user, SqlBackend backend, BankAccount selectedBankAccount, List<String> historyInputs) throws SQLException {
        this.selectedBankAccount = selectedBankAccount;

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommand(user));
        commandMap.put(Commands.HELP, new HelpCommand());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommand());
        commandMap.put(Commands.HISTORY, new HistoryCommand(historyInputs));

        commandMap.put(Commands.DEPOSIT, new DepositCommand(user, backend, this.selectedBankAccount));
        commandMap.put(Commands.WITHDRAW, new WithdrawCommand(user, backend, this.selectedBankAccount));
        commandMap.put(Commands.CONVERT, new ConvertCommand(backend, this.selectedBankAccount));

        // Sprachbefehle
        commandMap.put(Commands.GETLANGUAGES, new GetLanguagesCommand());
        commandMap.put(Commands.SETLANGUAGE, new SetLanguageCommand());

        // Kategoriebefehle
        commandMap.put(Commands.CREATECATEGORY, new CreateCategoryCommand(user, backend));
        commandMap.put(Commands.GETCATEGORIES, new GetCategoryCommand(user, backend));
        commandMap.put(Commands.DELETECATEGORY, new DeleteCategoryCommand(user, backend));

        //Bankkonto Befehle
        commandMap.put(Commands.CREATEBANKACCOUNT, new CreateBankAccountCommand(user, backend));
        commandMap.put(Commands.GETBANKACCOUNT, new GetBankAccountCommand(user, backend));
        commandMap.put(Commands.DELETEBANKACCOUNT, new DeleteBankAccountCommand(user, backend));
        commandMap.put(Commands.RENAMEBANKACCOUNT, new RenameBankAccountCommand(user, backend));
        commandMap.put(Commands.SWITCHBANKACCOUNT, new SwitchBankAccountCommand(user, backend, this));

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

    public BankAccount getSelectedBankAccount() {
        return selectedBankAccount;
    }

    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
        for (Command command : commandMap.values()) {
            if (command instanceof BankAccountAware) {
                ((BankAccountAware) command).setSelectedBankAccount(bankAccount);
            }
        }
    }

}