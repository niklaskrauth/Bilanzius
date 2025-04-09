package org.bilanzius.commands;

import org.bilanzius.commands.implementations.BankAccountAware;
import org.bilanzius.commands.implementations.ReportCommand;
import org.bilanzius.commands.implementations.bilanzius.BilanziusCommand;
import org.bilanzius.commands.implementations.convert.ConvertCommand;
import org.bilanzius.commands.implementations.createBankAccount.CreateBankAccountCommand;
import org.bilanzius.commands.implementations.createCategory.CreateCategoryCommand;
import org.bilanzius.commands.implementations.deleteBankAccount.DeleteBankAccountCommand;
import org.bilanzius.commands.implementations.deleteCategory.DeleteCategoryCommand;
import org.bilanzius.commands.implementations.deposit.DepositCommand;
import org.bilanzius.commands.implementations.exit.ExitCommand;
import org.bilanzius.commands.implementations.getBankAccount.GetBankAccountCommand;
import org.bilanzius.commands.implementations.getCategory.GetCategoryCommand;
import org.bilanzius.commands.implementations.getLanguage.GetLanguagesCommand;
import org.bilanzius.commands.implementations.help.HelpCommand;
import org.bilanzius.commands.implementations.history.HistoryCommand;
import org.bilanzius.commands.implementations.log.LogCommand;
import org.bilanzius.commands.implementations.renameBankAccount.RenameBankAccountCommand;
import org.bilanzius.commands.implementations.setLanguage.SetLanguageCommand;
import org.bilanzius.commands.implementations.switchBankAccount.SwitchBankAccountCommand;
import org.bilanzius.commands.implementations.withdraw.WithdrawCommand;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.Localization;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CommandController
{
    private final Map<Commands, Command> knownCommands;
    private final Localization localization = Localization.getInstance();
    private BankAccount selectedBankAccount;

    private final User user;

    public CommandController(User user,
                             BankAccount selectedBankAccount,
                             List<String> historyInputs)
    {
        this.user = user;
        this.selectedBankAccount = selectedBankAccount;

        //Hier werden die einzelnen Befehle über das Enum auf die Klassen gemappt
        this.knownCommands = new EnumMap<>(Commands.class);

        register(Commands.EXIT, new ExitCommand(user));
        register(Commands.HELP, new HelpCommand());
        register(Commands.BILANZIUS, new BilanziusCommand());
        register(Commands.HISTORY, new HistoryCommand(historyInputs));

        register(Commands.DEPOSIT, new DepositCommand(user, this.selectedBankAccount));
        register(Commands.WITHDRAW, new WithdrawCommand(user, this.selectedBankAccount));
        register(Commands.CONVERT, new ConvertCommand(this.selectedBankAccount));
        register(Commands.REPORT, new ReportCommand(user));

        // Sprachbefehle
        register(Commands.GETLANGUAGES, new GetLanguagesCommand());
        register(Commands.SETLANGUAGE, new SetLanguageCommand());

        // Kategoriebefehle
        register(Commands.CREATECATEGORY, new CreateCategoryCommand(DatabaseProvider.getCategoryService(), user));
        register(Commands.GETCATEGORIES, new GetCategoryCommand(user));
        register(Commands.DELETECATEGORY, new DeleteCategoryCommand(user));

        //Bankkonto Befehle
        register(Commands.CREATEBANKACCOUNT, new CreateBankAccountCommand(user));
        register(Commands.GETBANKACCOUNT, new GetBankAccountCommand(user));
        register(Commands.DELETEBANKACCOUNT, new DeleteBankAccountCommand(user));
        register(Commands.RENAMEBANKACCOUNT, new RenameBankAccountCommand(user));
        register(Commands.SWITCHBANKACCOUNT, new SwitchBankAccountCommand(user, this));
        register(Commands.LOG, new LogCommand(this));
    }

    public String handleInput(String input)
    {
        String[] parts = input.split(" ", 2);
        String commandStr = parts[0];
        String[] arguments = parseArguments(parts);

        Commands command = Commands.fromString(commandStr);
        Command commandService = knownCommands.get(command);

        if (commandService != null) {
            return commandService.execute(arguments);
        }

        return localization.getMessage("unknown_command");
    }

    public BankAccount getSelectedBankAccount()
    {
        return selectedBankAccount;
    }

    public User getUser()
    {
        return user;
    }

    public void setSelectedBankAccount(BankAccount bankAccount)
    {
        this.selectedBankAccount = bankAccount;
        for (Command command : knownCommands.values()) {
            if (command instanceof BankAccountAware) {
                ((BankAccountAware) command).setSelectedBankAccount(bankAccount);
            }
        }
    }

    private String[] parseArguments(String[] parts)
    {
        return parts.length > 1 ? parts[1].split(" ") : new String[0];
    }

    private void register(Commands type, Command commandImpl)
    {
        this.knownCommands.put(type, commandImpl);
    }

}