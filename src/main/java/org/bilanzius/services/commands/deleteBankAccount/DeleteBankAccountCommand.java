package org.bilanzius.services.commands.deleteBankAccount;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.services.Command;
import org.bilanzius.utils.Localization;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static org.bilanzius.utils.ValidateDelete.validateDeleteAction;

public class DeleteBankAccountCommand implements Command {

    private User user;
    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final Map<DeleteBankAccountCommandArguments, Function<String, String>> commandMap = new HashMap<>();
    private final Localization localization = Localization.getInstance();

    public DeleteBankAccountCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.userService = SqliteUserDatabaseService.getInstance(backend);

        commandMap.put(DeleteBankAccountCommandArguments.ALL, s -> deleteAllBankAccounts());
        commandMap.put(DeleteBankAccountCommandArguments.NAME, this::deleteBankAccountByName);
    }

    @Override
    public String execute(String[] arguments) {

        DeleteBankAccountCommandArguments argument;
        Function<String, String> command;

        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", DeleteBankAccountCommandArguments.getAllArguments());
        }

        argument = DeleteBankAccountCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", DeleteBankAccountCommandArguments.getAllArguments());
        }

        command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DeleteBankAccountCommandArguments.getAllArguments());
    }

    private String deleteBankAccountByName(String name) {

        BankAccount bankAccount;
        List<BankAccount> bankAccounts;


        try {
            bankAccount = bankAccountService.getBankAccountOfUserByName(user, name).stream().findFirst().orElse(null);
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (bankAccount == null) {
            return localization.getMessage("no_bank_account_with_name", name);
        }

        if (user.getMainBankAccountId() == bankAccount.getAccountId()) {
            return localization.getMessage("cannot_delete_main_bank_account");
        }

        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            return localization.getMessage("bank_account_not_empty", bankAccount.getName());
        }

        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(user, 100);
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (bankAccounts.size() == 1) {
            return localization.getMessage("cannot_delete_last_bank_account");
        }

        if (validateDeleteAction(localization.getMessage("ask_for_deletion_bank_account", bankAccount.getName()))) {
            return localization.getMessage("no_bank_account_deleted");
        }

        try {
            bankAccountService.deleteBankAccount(bankAccount);
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        return localization.getMessage("bank_account_deleted", bankAccount.getName());
    }

    private String deleteAllBankAccounts() {

        List<BankAccount> bankAccounts;
        BankAccount mainBankAccount;
        Optional<User> userOptional;
        List<String> deletedBankAccounts = new ArrayList<>();

        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(this.user, 100).stream().toList();
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (bankAccounts.isEmpty()) {
            return localization.getMessage("no_bank_accounts");
        }

        try {
            userOptional = userService.findUser(this.user.getId()).stream().findFirst();
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }
        this.user = userOptional.orElseThrow();

        try {
            mainBankAccount = bankAccountService.getBankAccount(this.user.getMainBankAccountId()).orElseThrow();
        } catch (DatabaseException e) {
            return localization.getMessage("database_error", e.toString());
        }

        if (validateDeleteAction(localization.getMessage("ask_for_deletion_all_bank_accounts", mainBankAccount.getName()))) {
            return localization.getMessage("no_bank_accounts_deleted");
        }

        bankAccounts.forEach(bankAccount -> {
            if (user.getMainBankAccountId() == bankAccount.getAccountId()) {
                return;
            }

            if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(localization.getMessage("bank_account_not_empty", bankAccount.getName()));
                return;
            }

            deletedBankAccounts.add(bankAccount.getName());
            try {
                bankAccountService.deleteBankAccount(bankAccount);
            } catch (DatabaseException e) {
                System.out.println(localization.getMessage("database_error", e.toString()));
            }

        });
        return localization.getMessage("deleted_possible_bank_accounts", String.join("\n", deletedBankAccounts));
    }
}
