package org.bilanzius.services.commands.deleteBankAccount;

import org.bilanzius.persistence.BankAccountService;
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

public class DeleteBankAccountCommand implements Command {
    private User user;
    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final Map<DeleteBankAccountCommandArguments, Function<String, String>> commandMap;
    private final Localization localization = Localization.getInstance();
    private final Scanner scanner;

    public DeleteBankAccountCommand(User user, SqlBackend backend) throws SQLException {
        this.user = user;
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.userService = SqliteUserDatabaseService.getInstance(backend);
        this.scanner = new Scanner(System.in);

        commandMap = new HashMap<>();
        commandMap.put(DeleteBankAccountCommandArguments.ALL, s -> deleteAllBankAccounts());
        commandMap.put(DeleteBankAccountCommandArguments.NAME, this::deleteBankAccountByName);
    }

    @Override
    public String execute(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return localization.getMessage("no_arguments_provided", DeleteBankAccountCommandArguments.getAllArguments());
        }

        DeleteBankAccountCommandArguments argument = DeleteBankAccountCommandArguments.fromString(arguments[0]);
        if (argument == null) {
            return localization.getMessage("unknown_argument", DeleteBankAccountCommandArguments.getAllArguments());
        }

        Function<String, String> command = commandMap.get(argument);
        if (command != null) {
            return command.apply(arguments.length > 1 ? arguments[1] : null);
        }

        return localization.getMessage("unknown_argument", DeleteBankAccountCommandArguments.getAllArguments());
    }

    private String deleteBankAccountByName(String name) {
        BankAccount bankAccount = bankAccountService.getBankAccountsOfUserByName(user, name).stream().findFirst().orElse(null);
        if (bankAccount == null) {
            return localization.getMessage("no_bank_account_with_name", name);
        }
        if (user.getMainBankAccountId() == bankAccount.getAccountId()) {
            return localization.getMessage("cannot_delete_main_bank_account");
        }
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            return localization.getMessage("bank_account_not_empty", bankAccount.getName());
        }
        if (bankAccountService.getBankAccountsOfUser(user, 100).size() == 1) {
            return localization.getMessage("cannot_delete_last_bank_account");
        }
        if (validateDeleteAction(localization.getMessage("ask_for_deletion_bank_account", bankAccount.getName()))) {
            return localization.getMessage("no_bank_account_deleted");
        }
        bankAccountService.deleteBankAccount(bankAccount);
        return localization.getMessage("bank_account_deleted", bankAccount.getName());
    }

    private String deleteAllBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.getBankAccountsOfUser(this.user, 100).stream().toList();
        if (bankAccounts.isEmpty()) {
            return localization.getMessage("no_bank_accounts");
        }
        Optional<User> userOptional = userService.findUser(this.user.getId()).stream().findFirst();
        this.user = userOptional.orElseThrow();
        BankAccount mainBankAccount = bankAccountService.getBankAccount(this.user.getMainBankAccountId()).orElseThrow();
        if (validateDeleteAction(localization.getMessage("ask_for_deletion_all_bank_accounts", mainBankAccount.getName()))) {
            return localization.getMessage("no_bank_accounts_deleted");
        }
        List<String> deletedBankAccounts = new ArrayList<>(List.of());
        bankAccounts.forEach(bankAccount -> {
            if (user.getMainBankAccountId() == bankAccount.getAccountId()) {
                return;
            }
            if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(localization.getMessage("bank_account_not_empty", bankAccount.getName()));
                return;
            }
            deletedBankAccounts.add(bankAccount.getName());
            bankAccountService.deleteBankAccount(bankAccount);
        });
        return localization.getMessage("deleted_possible_bank_accounts", String.join("\n", deletedBankAccounts));
    }

    private boolean validateDeleteAction(String message){
        System.out.println(message + " (yes/no): ");
        String response = this.scanner.nextLine().trim().toLowerCase();
        return !response.equals("yes") && !response.equals("y");
    }
}
