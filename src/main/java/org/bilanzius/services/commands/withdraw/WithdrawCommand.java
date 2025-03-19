package org.bilanzius.services.commands.withdraw;

import org.bilanzius.persistence.*;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.services.Command;
import org.bilanzius.services.commands.BankAccountAware;
import org.bilanzius.utils.Localization;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WithdrawCommand implements Command, BankAccountAware
{

    private User user;
    private final Map<WithdrawCommandArgument, Function<String[], String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();
    private final TransactionService transactionService;
    private BankAccount selectedBankAccount;
    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;

    public WithdrawCommand(User user, BankAccount selectedBankAccount)
    {
        this.user = user;
        this.transactionService = DatabaseProvider.getTransactionService();
        this.categoryService = DatabaseProvider.getCategoryService();
        this.bankAccountService = DatabaseProvider.getBankAccountService();
        this.selectedBankAccount = selectedBankAccount;

        commandMap.put(WithdrawCommandArgument.WITHDRAW, this::withdrawMoney);
    }

    @Override
    public void setSelectedBankAccount(BankAccount bankAccount)
    {
        this.selectedBankAccount = bankAccount;
    }

    @Override
    public String execute(String[] arguments)
    {

        WithdrawCommandArgument argument;
        Function<String[], String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", WithdrawCommandArgument.getAllArguments());
        }

        if (arguments.length != 2 && arguments.length != 4) {
            return localization.getMessage("withdraw_command_usage");
        }

        argument =
                WithdrawCommandArgument.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", WithdrawCommandArgument.getAllArguments());
        }

        command =
                commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments);
        }

        return localization.getMessage("withdraw_command_usage");
    }

    private String withdrawMoney(String[] arguments)
    {

        BigDecimal withdrawMoney;
        String categoryName;
        Category category;
        Transaction transaction;

        try {
            withdrawMoney = BigDecimal.valueOf(Math.abs(Double.parseDouble(arguments[1])));

            if (arguments.length == 4 && (WithdrawCommandArgument.CATEGORY.getArgument().equals(arguments[2]) ||
                    WithdrawCommandArgument.CATEGORY.getArgumentShort().equals(arguments[2]))) {

                categoryName = arguments[3];
                category = categoryService.getCategoryOfUserByName(user, categoryName).orElse(null);

                if (category == null) {
                    return localization.getMessage("no_category_with_name", categoryName);
                }

                transaction = Transaction.create(user, selectedBankAccount, category, withdrawMoney.negate(),
                        Instant.now(), "Withdraw " + withdrawMoney);

                category.setAmountSpent(category.getAmountSpent().add(transaction.getMoney()));
                this.checkCategoryBudget(category);
                categoryService.updateCategory(category);

                transactionService.saveTransaction(transaction);

                this.selectedBankAccount.setBalance(this.selectedBankAccount.getBalance().add(transaction.getMoney()));
                bankAccountService.updateBankAccount(this.selectedBankAccount);
                setSelectedBankAccount(bankAccountService.getBankAccount(
                        this.selectedBankAccount.getAccountId()).orElseThrow());

            } else if (arguments.length == 2 && (WithdrawCommandArgument.WITHDRAW.getArgument().equals(arguments[0]) ||
                    WithdrawCommandArgument.WITHDRAW.getArgumentShort().equals(arguments[0]))) {

                transaction = Transaction.create(
                        user, selectedBankAccount, withdrawMoney.negate(), "Withdraw " + withdrawMoney);

                transactionService.saveTransaction(transaction);

            } else {
                return localization.getMessage("withdraw_command_usage");
            }

            return checkBalance();
        } catch (
                NumberFormatException e) {
            return localization.getMessage("invalid_amount");
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }
    }

    private void checkCategoryBudget(Category category)
    {
        if (category.getAmountSpent().compareTo(category.getBudget()) > 0) {
            System.out.println(localization.getMessage("category_exceeded_budget", category.toString()));
        }
    }

    private String checkBalance()
    {

        BigDecimal balance;

        try {
            balance = bankAccountService.getBankAccount(selectedBankAccount.getAccountId()).orElseThrow().getBalance();
        } catch (
                DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return localization.getMessage("withdraw_successful_dept", balance);
        } else {
            return localization.getMessage("withdraw_successful", balance);
        }
    }
}