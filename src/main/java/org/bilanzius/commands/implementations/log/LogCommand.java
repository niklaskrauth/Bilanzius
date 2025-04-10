package org.bilanzius.commands.implementations.log;

import org.bilanzius.commands.CommandController;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.commands.Command;
import org.bilanzius.utils.Localization;

public class LogCommand implements Command
{

    private static final int ENTRIES_PER_PAGE = 10;

    private final TransactionService transactionService;
    private final CommandController commandController;

    public LogCommand(CommandController commandController)
    {
        this.transactionService = DatabaseProvider.getTransactionService();
        this.commandController = commandController;
    }

    @Override
    public String execute(String[] arguments)
    {
        var user = this.commandController.getUser();
        var account = this.commandController.getSelectedBankAccount();
        var logs = transactionService.getTransactions(user, account, ENTRIES_PER_PAGE, 0);

        StringBuilder stringBuilder = new StringBuilder();

        logs.stream()
            .map(x -> x.getMoney() + " - " + x.getDescription() + " (" + Localization.getInstance().formatInstant(x.getCreated()) + ")")
            .forEach(x -> stringBuilder.append(x).append(System.lineSeparator()));

        return stringBuilder.toString();
    }
}
