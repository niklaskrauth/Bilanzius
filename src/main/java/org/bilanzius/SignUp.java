package org.bilanzius;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class SignUp {
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final Localization localization = Localization.getInstance();

    public SignUp(SqlBackend backend) throws SQLException {
        this.userService = SqliteUserDatabaseService.getInstance(backend);
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
    }

    public User waitUntilLoggedIn (Scanner scanner) {

        while (true) {
            System.out.println("----------------------------------------------------------------------------------");

            System.out.println(localization.getMessage("login_or_register"));
            String loginOrRegister = scanner.nextLine();

            if (loginOrRegister.equals("1")) {
                System.out.println(localization.getMessage("login"));

                Optional<User> optionalUser = login(scanner);

                if(optionalUser.isPresent()) {
                    return optionalUser.get();
                } else {
                    System.out.println(localization.getMessage("database_error"));
                }

            } else if (loginOrRegister.equals("2")) {
                System.out.println(localization.getMessage("register"));

                Optional<User> optionalUser = register(scanner);

                if(optionalUser.isPresent()) {
                    return optionalUser.get();
                } else {
                    System.out.println(localization.getMessage("database_error"));
                }

            } else {
                System.out.println(localization.getMessage("invalid_input"));
            }
        }
    }

    public Optional<User> login(Scanner scanner){

        Optional<User> loggedInUser = Optional.empty();

        while(loggedInUser.isEmpty()) {

            System.out.println(localization.getMessage("username"));
            String username = scanner.nextLine();
            Optional<User> userOptional;

            try {
                userOptional = userService.findUserWithName(username);
            } catch (DatabaseException e) {
                System.out.println(localization.getMessage("database_error"));
                return Optional.empty();
            }

            if (userOptional.isPresent()) {

                User user = userOptional.get();
                System.out.println(localization.getMessage("password"));
                String password = scanner.nextLine();

                if (fromPlainText(password).equals(user.getHashedPassword())) {

                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println(localization.getMessage("greeting_user", user.getUsername()));

                    loggedInUser = Optional.of(user);

                } else {
                    System.out.println(localization.getMessage("wrongPassword"));
                }

            } else {
                System.out.println(localization.getMessage("wrongUsername"));
            }

        }
        return loggedInUser;
    }

    public Optional<User> register(Scanner scanner){

        Boolean userExists = null; // null = not existing (wrong inputs), true = already exists in DB, false = does not exist in DB and inputs are correct
        User newUser = null;

        while(userExists == null || userExists) {

            System.out.println(localization.getMessage("username"));
            String username = scanner.nextLine();

            User foundUser = userService.findUserWithName(username).orElse(null);

            if (foundUser != null) {

                System.out.println(localization.getMessage("user_exists", foundUser.getUsername()));
                userExists = true;

            } else {

                System.out.println(localization.getMessage("password"));
                String password = scanner.nextLine();

                System.out.println(localization.getMessage("repeat_password"));
                String repeatPassword = scanner.nextLine();

                if (password.equals(repeatPassword)) {

                    newUser = User.createUser(username, fromPlainText(password));

                    try {
                        userService.createUser(newUser);
                    } catch (DatabaseException e) {
                        return Optional.empty();
                    }

                    System.out.println(localization.getMessage("user_created", newUser.getUsername()));
                    userExists = false;

                } else {
                    System.out.println(localization.getMessage("passwords_do_not_match"));
                }
            }
        }
        return Optional.of(newUser);
    }

    public Optional<BankAccount> waitUntilBankAccountSelect(Scanner scanner, User user) {
        
        List<BankAccount> bankAccounts;
        Optional<BankAccount> bankAccount;
        BankAccount createdBankAccount;
        
        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(user, 5);
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }
        
        if (bankAccounts.size() == 1){
            BankAccount account;
            
            try {
                account = bankAccountService.getBankAccountsOfUser(user, 1).getFirst();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            
            System.out.println(localization.getMessage("only_one_bank_account", account.getName()));
            return Optional.of(account);
        }
        
        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(user, 1);
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }

        if (!bankAccounts.isEmpty()) {
            System.out.println(localization.getMessage("select_bank_account"));
            
            try {
                bankAccountService.getBankAccountsOfUser(user, 10).forEach(account -> System.out.println(account.getName()));
            } catch (DatabaseException e) {
                System.out.println(localization.getMessage("database_error", e.toString()));
                return Optional.empty();
            }

            System.out.println("----------------------------------------------------------------------------------");
            System.out.println(localization.getMessage("bank_account_name"));
            
            while (true) {
                
                String bankAccountName = scanner.nextLine();
                BankAccount account;
                
                try {
                    account = bankAccountService.getBankAccountsOfUserByName(user, bankAccountName).orElse(null);
                } catch (DatabaseException e) {
                    System.out.println(localization.getMessage("database_error", e.toString()));
                    return Optional.empty();
                }
                
                if (account != null) {
                    return Optional.of(account);
                }
                
                System.out.println(localization.getMessage("wrong_bank_account_name"));
            }
        }

        System.out.println(localization.getMessage("no_bank_accounts_yet"));
        System.out.println("----------------------------------------------------------------------------------");

        System.out.println(localization.getMessage("bank_account_name"));
        String bankAccountName = scanner.nextLine();

        createdBankAccount = BankAccount.create(user, bankAccountName);
        
        try {
            bankAccountService.createBankAccount(createdBankAccount);
            bankAccount = bankAccountService.getBankAccountsOfUserByName(user, createdBankAccount.getName()).stream().findFirst();
            if (bankAccount.isEmpty()) {
                return Optional.empty();
            }
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }

        if (user.getMainBankAccountId() == 0) {
            user.setMainAccountId(bankAccount.get().getAccountId());
            
            try {
                userService.updateUserMainAccountId(user);
            } catch (DatabaseException e) {
                System.out.println(localization.getMessage("database_error", e.toString()));
                return Optional.empty();
            }
        }

        System.out.println(localization.getMessage("bank_account_created", bankAccountName));
        
        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(user, 1);
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }

        return Optional.ofNullable(bankAccounts.getFirst());
    }
}
