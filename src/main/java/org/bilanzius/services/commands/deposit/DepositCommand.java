package org.bilanzius.services.commands.deposit;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DepositCommand implements Command, BankAccountAware {
    private User user;
    private final Map<DepositCommandArguments, Function<String, String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();
    private final TransactionService transactionService;
    private BankAccount selectedBankAccount;
    private final BankAccountService bankAccountService;

    public DepositCommand(User user, BankAccount selectedBankAccount) {
        this.user = user;
        this.selectedBankAccount = selectedBankAccount;
        this.transactionService = DatabaseProvider.getTransactionService();
        this.bankAccountService = DatabaseProvider.getBankAccountService();

        commandMap.put(DepositCommandArguments.DEPOSIT, this::depositMoney);
    }

    @Override
    public void setSelectedBankAccount(BankAccount bankAccount) {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments) {

        DepositCommandArguments argument;
        Function<String, String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", DepositCommandArguments.getAllArguments());
        }

        argument = DepositCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", DepositCommandArguments.getAllArguments());
        }

        command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DepositCommandArguments.getAllArguments());
    }

    private String depositMoney(String argument) {

        BigDecimal depositMoney;
        BigDecimal balance;

        try {

            depositMoney = BigDecimal.valueOf(Double.parseDouble(argument));
            depositMoney = depositMoney.abs();
            transactionService.saveTransaction(Transaction.create(
                    user, selectedBankAccount, depositMoney, Instant.now(), "Deposit" + depositMoney));

            balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();

            return localization.getMessage("deposit_successful", balance);
        } catch (NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }
    }
}
