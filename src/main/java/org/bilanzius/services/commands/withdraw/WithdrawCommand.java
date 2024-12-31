package org.bilanzius.services.commands.withdraw;

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

public class WithdrawCommand implements Command, BankAccountAware {

    private User user;
    private final Map<WithdrawCommandArgument, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final TransactionService transactionService;
    private BankAccount selectedBankAccount;
    private final BankAccountService bankAccountService;


    public WithdrawCommand(User user, SqlBackend backend, BankAccount selectedBankAccount) throws SQLException {
        this.user = user;
        this.transactionService = SqliteTransactionService.getInstance(backend);
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.selectedBankAccount = selectedBankAccount;

        commandMap = new HashMap<>();
        commandMap.put(WithdrawCommandArgument.WITHDRAW, this::withdrawMoney);

    }

    @Override
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided",
                    WithdrawCommandArgument.getAllArguments());
        }

        WithdrawCommandArgument argument = WithdrawCommandArgument.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument",
                    WithdrawCommandArgument.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", WithdrawCommandArgument.getAllArguments());
    }

    private String withdrawMoney(String argument) {

        double withdrawMoney;
        String outputText;

        try {

            withdrawMoney = Double.parseDouble(argument);
            withdrawMoney = Math.abs(withdrawMoney);
            transactionService.saveTransaction(Transaction.create(user, selectedBankAccount, -withdrawMoney, "Withdraw" + withdrawMoney));

            double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
            if (balance < 0) {
                outputText = localization.getMessage("withdraw_successful_dept", balance);
            } else {
                outputText = localization.getMessage("withdraw_successful", balance);
            }

            return outputText;
        } catch (NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        }
    }
}
