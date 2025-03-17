package org.bilanzius.commandController;

import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.services.commands.ReportCommand;
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
import org.bilanzius.services.commands.log.LogCommand;
import org.bilanzius.services.commands.renameBankAccount.RenameBankAccountCommand;
import org.bilanzius.services.commands.setLanguage.SetLanguageCommand;
import org.bilanzius.services.commands.switchBankAccount.SwitchBankAccountCommand;
import org.bilanzius.services.commands.withdraw.WithdrawCommand;
import org.bilanzius.utils.Localization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandController {
    private final Map<Commands, Command> commandMap;
    private final Localization localization = Localization.getInstance();
    private BankAccount selectedBankAccount;

    private final User user;

    public CommandController(User user, BankAccount selectedBankAccount, List<String> historyInputs) {
        this.user = user;
        this.selectedBankAccount = selectedBankAccount;

        commandMap = new HashMap<>();

        commandMap.put(Commands.EXIT, new ExitCommand(user));
        commandMap.put(Commands.HELP, new HelpCommand());
        commandMap.put(Commands.BILANZIUS, new BilanziusCommand());
        commandMap.put(Commands.HISTORY, new HistoryCommand(historyInputs));

        commandMap.put(Commands.DEPOSIT, new DepositCommand(user, this.selectedBankAccount));
        commandMap.put(Commands.WITHDRAW, new WithdrawCommand(user, this.selectedBankAccount));
        commandMap.put(Commands.CONVERT, new ConvertCommand(this.selectedBankAccount));
        commandMap.put(Commands.REPORT, new ReportCommand(user));

        // Sprachbefehle
        commandMap.put(Commands.GETLANGUAGES, new GetLanguagesCommand());
        commandMap.put(Commands.SETLANGUAGE, new SetLanguageCommand());

        // Kategoriebefehle
        commandMap.put(Commands.CREATECATEGORY, new CreateCategoryCommand(user));
        commandMap.put(Commands.GETCATEGORIES, new GetCategoryCommand(user));
        commandMap.put(Commands.DELETECATEGORY, new DeleteCategoryCommand(user));

        //Bankkonto Befehle
        commandMap.put(Commands.CREATEBANKACCOUNT, new CreateBankAccountCommand(user));
        commandMap.put(Commands.GETBANKACCOUNT, new GetBankAccountCommand(user));
        commandMap.put(Commands.DELETEBANKACCOUNT, new DeleteBankAccountCommand(user));
        commandMap.put(Commands.RENAMEBANKACCOUNT, new RenameBankAccountCommand(user));
        commandMap.put(Commands.SWITCHBANKACCOUNT, new SwitchBankAccountCommand(user, this));
        commandMap.put(Commands.LOG, new LogCommand(this));

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

    public User getUser() {
        return user;
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