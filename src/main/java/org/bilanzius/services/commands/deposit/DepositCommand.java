package org.bilanzius.services.commands.deposit;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.persistence.sql.SqliteTransactionService;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DepositCommand implements Command, BankAccountAware {

    private User user;
    private final Map<DepositCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final TransactionService transactionService;
    private BankAccount selectedBankAccount;
    private final BankAccountService bankAccountService;


    public DepositCommand(User user, SqlBackend backend, BankAccount selectedBankAccount) throws SQLException {
        this.user = user;
        this.selectedBankAccount = selectedBankAccount;
        this.transactionService = SqliteTransactionService.getInstance(backend);
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);

        commandMap = new HashMap<>();
        commandMap.put(DepositCommandArguments.DEPOSIT, this::depositMoney);

    }

    @Override
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", DepositCommandArguments.getAllArguments());
        }

        DepositCommandArguments argument = DepositCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", DepositCommandArguments.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DepositCommandArguments.getAllArguments());
    }

    private String depositMoney(String argument) {

        double depositMoney;

        try {

            depositMoney = Double.parseDouble(argument);
            depositMoney = Math.abs(depositMoney);
            transactionService.saveTransaction(Transaction.create(user, selectedBankAccount, depositMoney, "Deposit" + depositMoney));

            double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();

            return localization.getMessage("deposit_successful", balance);
        } catch (NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        }
    }
}
