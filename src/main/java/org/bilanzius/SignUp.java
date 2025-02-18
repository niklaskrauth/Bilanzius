package org.bilanzius;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteBankAccountService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
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

    public User waitUntilLoggedIn (Scanner input) {

        while (true) {
            System.out.println("----------------------------------------------------------------------------------");

            System.out.println(localization.getMessage("login_or_register"));
            String stringInput = input.nextLine();

            if (stringInput.equals("1")) {
                System.out.println(localization.getMessage("login"));
                return login(input);
            } else if (stringInput.equals("2")) {
                System.out.println(localization.getMessage("register"));
                return register(input);
            } else {
                System.out.println(localization.getMessage("invalid_input"));
            }
        }
    }

    public User login(Scanner input){

        User loggedInUser = null;

        while(loggedInUser == null) {

            System.out.println(localization.getMessage("username"));
            String stringInput = input.nextLine();

            Optional<User> userOptional = userService.findUserWithName(stringInput);
            if (userOptional.isPresent()) {

                User user = userOptional.get();
                System.out.println(localization.getMessage("password"));
                stringInput = input.nextLine();

                if (fromPlainText(stringInput).equals(user.getHashedPassword())) {

                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println(localization.getMessage("greeting_user", user.getUsername()));

                    loggedInUser = user;

                } else {
                    System.out.println(localization.getMessage("wrongPassword"));
                }

            } else {
                System.out.println(localization.getMessage("wrongUsername"));
            }

        }

        return loggedInUser;
    }

    public User register(Scanner input){

        Boolean userExists = null; // null = not existing (wrong inputs), true = already exists in DB, false = does not exist in DB and inputs are correct
        User newUser = null;

        while(userExists == null || userExists) {

            System.out.println(localization.getMessage("username"));
            String stringInput = input.nextLine();

            User foundUser = userService.findUserWithName(stringInput).orElse(null);

            if (foundUser != null) {

                System.out.println(localization.getMessage("user_exists", foundUser.getUsername()));
                userExists = true;

            } else {

                System.out.println(localization.getMessage("password"));
                String password = input.nextLine();

                System.out.println(localization.getMessage("repeat_password"));
                String repeatPassword = input.nextLine();

                if (password.equals(repeatPassword)) {

                    newUser = User.createUser(stringInput, fromPlainText(password));
                    userService.createUser(newUser);
                    System.out.println(localization.getMessage("user_created", newUser.getUsername()));
                    userExists = false;

                } else {
                    System.out.println(localization.getMessage("passwords_do_not_match"));
                }
            }
        }
        return newUser;
    }

    public BankAccount waitUntilBankAccountSelect(Scanner input, User user) {
        if ((long) bankAccountService.getBankAccountsOfUser(user, 5).size() == 1){
            BankAccount account = bankAccountService.getBankAccountsOfUser(user, 1).getFirst();
            System.out.println(localization.getMessage("only_one_bank_account", account.getName()));
            return account;
        }
        if (!bankAccountService.getBankAccountsOfUser(user, 1).isEmpty()) {
            System.out.println(localization.getMessage("select_bank_account"));
            bankAccountService.getBankAccountsOfUser(user, 10).forEach(account -> System.out.println(account.getName()));
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println(localization.getMessage("bank_account_name"));
            while (true) {
                String stringInput = input.nextLine();
                BankAccount account = bankAccountService.getBankAccountsOfUserByName(user, stringInput).orElse(null);
                if (account != null) {
                    return account;
                }
                System.out.println(localization.getMessage("wrong_bank_account_name"));
            }
        }
        System.out.println(localization.getMessage("no_bank_accounts_yet"));
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(localization.getMessage("bank_account_name"));
        String stringInput = input.nextLine();
        bankAccountService.createBankAccount(BankAccount.create(user, stringInput));
        System.out.println(localization.getMessage("bank_account_created", stringInput));
        return bankAccountService.getBankAccountsOfUser(user, 1).getFirst();
    }
}
