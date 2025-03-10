package org.bilanzius;

import org.bilanzius.cli.IOContext;
import org.bilanzius.cli.Question;
import org.bilanzius.cli.QuestionException;
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
    private final int MAX_BANK_ACCOUNTS = 3;

    public SignUp(SqlBackend backend) throws SQLException {
        this.userService = SqliteUserDatabaseService.getInstance(backend);
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
    }

    public User waitUntilLoggedIn(IOContext context) {
        Optional<User> optionalUser;

        context.lineSeperator();

        var loginOrRegister = context.askUser(Question.create()
                .question(localization.getMessage("login_or_register"))
                .defaultValue("1")
                .validator(input -> {
                    if (!input.equals("1") && !input.equals("2")) {
                        throw new QuestionException(localization.getMessage("invalid_input"));
                    }
                })
                .build());

        if (loginOrRegister.equals("1")) {
            context.printLocalized("login");

            optionalUser = login(context);

            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        } else {
            context.printLocalized("register");

            optionalUser = register(context);

            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        }

        context.printLocalized("database_error");
        return waitUntilLoggedIn(context);
    }

    public Optional<User> login(IOContext context) {
        Optional<User> loggedInUser;

        do {
            loggedInUser = tryLogin(context);
        } while (loggedInUser.isEmpty());

        return loggedInUser;
    }

    public Optional<User> tryLogin(IOContext context) {
        var username = context.askUser(Question.create()
                .question(localization.getMessage("username"))
                .build());

        Optional<User> userOptional;
        User user;

        try {
            userOptional = userService.findUserWithName(username);
        } catch (DatabaseException e) {
            context.printLocalized("database_error");
            return Optional.empty();
        }

        if (userOptional.isEmpty()) {
            context.printLocalized("wrongUsername");
            return Optional.empty();
        }

        user = userOptional.get();
        var password = context.askUser(Question.create()
                .question(localization.getMessage("password"))
                .build());

        if (!fromPlainText(password).equals(user.getHashedPassword())) {
            context.printLocalized("wrongPassword");
            return Optional.empty();
        }

        context.lineSeperator();
        context.printLocalized("greeting_user", user.getUsername());

        return Optional.of(user);
    }

    public Optional<User> register(IOContext context) {
        Boolean userExists = null; // null = not existing (wrong inputs), true = already exists in DB, false = does not exist in DB and inputs are correct
        Optional<User> user = Optional.empty();
        Optional<User> foundUser;
        User newUser;

        while (userExists == null || userExists) {
            String username = context.askUser(Question.create()
                    .question(localization.getMessage("username"))
                    .build());

            foundUser = userService.findUserWithName(username);

            if (foundUser.isPresent()) {
                context.printLocalized("user_exists", foundUser.get().getUsername());
                userExists = true;
                continue;
            }

            String password = context.askUser(Question.create()
                    .question(localization.getMessage("password"))
                    .build());
            String repeatPassword = context.askUser(Question.create()
                    .question(localization.getMessage("repeat_password"))
                    .build());

            if (!password.equals(repeatPassword)) {
                context.printLocalized("passwords_do_not_match");
                continue;
            }

            newUser = User.createUser(username, fromPlainText(password));

            try {
                userService.createUser(newUser);
                user = userService.findUserWithName(username);
            } catch (DatabaseException e) {
                return Optional.empty();
            }

            context.printLocalized("user_created", newUser.getUsername());
            userExists = false;
        }
        return user;
    }

    public Optional<BankAccount> waitUntilBankAccountSelect(Scanner scanner, User user) {

        List<BankAccount> bankAccounts;
        Optional<BankAccount> bankAccount;
        BankAccount createdBankAccount;

        try {
            bankAccounts = bankAccountService.getBankAccountsOfUser(user, MAX_BANK_ACCOUNTS);
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }

        if (bankAccounts.size() == 1) {
            BankAccount account;

            account = bankAccounts.getFirst();

            System.out.println(localization.getMessage("only_one_bank_account", account.getName()));
            return Optional.of(account);
        }

        if (!bankAccounts.isEmpty()) {
            System.out.println(localization.getMessage("select_bank_account"));

            bankAccounts.forEach(account -> System.out.println(account.getName()));

            System.out.println(localization.getMessage("line_splitter"));
            System.out.println(localization.getMessage("bank_account_name"));

            while (true) {

                String bankAccountName = scanner.nextLine();
                Optional<BankAccount> account;

                try {
                    account = bankAccountService.getBankAccountOfUserByName(user, bankAccountName);
                } catch (DatabaseException e) {
                    System.out.println(localization.getMessage("database_error", e.toString()));
                    return Optional.empty();
                }

                if (account.isPresent()) {
                    return account;
                }

                System.out.println(localization.getMessage("wrong_bank_account_name"));
            }
        }

        System.out.println(localization.getMessage("no_bank_accounts_yet"));
        System.out.println(localization.getMessage("line_splitter"));

        System.out.println(localization.getMessage("bank_account_name"));
        String bankAccountName = scanner.nextLine();

        createdBankAccount = BankAccount.create(user, bankAccountName);

        try {
            bankAccountService.createBankAccount(createdBankAccount);
            bankAccount = bankAccountService.getBankAccountOfUserByName(user, createdBankAccount.getName());
            if (bankAccount.isEmpty()) {
                return Optional.empty();
            }
        } catch (DatabaseException e) {
            System.out.println(localization.getMessage("database_error", e.toString()));
            return Optional.empty();
        }

        System.out.println(localization.getMessage("bank_account_created", bankAccountName));

        if (user.getMainBankAccountId() == 0) {
            user.setMainAccountId(bankAccount.get().getAccountId());

            try {
                userService.updateUserMainAccountId(user);
                user = userService.findUser(user.getId()).orElse(user);
            } catch (DatabaseException e) {
                System.out.println(localization.getMessage("database_error", e.toString()));
                return Optional.empty();
            }
        }

        System.out.println(localization.getMessage("set_user_main_bank_account", user.getUsername(), bankAccountName));

        return bankAccount;
    }
}
