package org.bilanzius.services.commands.withdraw;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.persistence.sql.SqliteCategoryService;
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
    private final Map<WithdrawCommandArgument, Function<String[], String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final TransactionService transactionService;
    private BankAccount selectedBankAccount;
    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;

    public WithdrawCommand(User user, SqlBackend backend, BankAccount selectedBankAccount) throws SQLException {
        this.user = user;
        this.transactionService = SqliteTransactionService.getInstance(backend);
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.selectedBankAccount = selectedBankAccount;
        this.categoryService = SqliteCategoryService.getInstance(backend);

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
            return localization.getMessage("no_arguments_provided", WithdrawCommandArgument.getAllArguments());
        }
        if (arguments.length != 2 && arguments.length != 4) {
            return localization.getMessage("withdraw_command_usage");
        }

        WithdrawCommandArgument argument = WithdrawCommandArgument.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", WithdrawCommandArgument.getAllArguments());
        }

        Function<String[], String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments);
        }

        return localization.getMessage("withdraw_command_usage");
    }

    private String withdrawMoney(String[] arguments) {
        try {
            double withdrawMoney = Math.abs(Double.parseDouble(arguments[1]));

            if (arguments.length == 4 && (WithdrawCommandArgument.CATEGORY.getArgument().equals(arguments[2]) ||
                    WithdrawCommandArgument.CATEGORY.getArgumentShort().equals(arguments[2]))) {
                String categoryName = arguments[3];
                Category category = categoryService.getCategoryOfUserByName(user, categoryName).orElse(null);
                if (category == null) {
                    return localization.getMessage("no_category_with_name", categoryName);
                }
                transactionService.saveTransaction(Transaction.create(user, selectedBankAccount, category, -withdrawMoney, "Withdraw " + withdrawMoney));
            } else if (arguments.length == 2 && (WithdrawCommandArgument.WITHDRAW.getArgument().equals(arguments[0]) ||
                    WithdrawCommandArgument.WITHDRAW.getArgumentShort().equals(arguments[0]))) {
                transactionService.saveTransaction(Transaction.create(user, selectedBankAccount, -withdrawMoney, "Withdraw " + withdrawMoney));
            } else {
                return localization.getMessage("withdraw_command_usage");
            }

            return checkBalance();
        } catch (NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        }
    }

    private String checkBalance() {
        double balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        if (balance < 0) {
            return localization.getMessage("withdraw_successful_dept", balance);
        } else {
            return localization.getMessage("withdraw_successful", balance);
        }
    }
}