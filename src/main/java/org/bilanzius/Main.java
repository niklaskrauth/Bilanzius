package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.sql.*;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.RestController;
import org.bilanzius.utils.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class Main {

    public static void main(String[] args) {
        // Connect to sqllite database
        var backend = new SqlBackend();
        UserService userService;
        Localization localization;
        SignUp signUp;

        RestController restController = new RestController();

        try {

            restController.mainRestController();

            Scanner scanner = new Scanner(System.in);

            backend.connect();
            userService = SqliteUserDatabaseService.getInstance(backend);
            localization = Localization.getInstance();

            signUp = new SignUp(backend);

            createTestUsers(userService);

            WelcomeUser.welcomeMessage();

            List<String> historyInputs = new ArrayList<>();
            User user;

            while (true) {

                user = signUp.waitUntilLoggedIn(scanner);

                System.out.println(localization.getMessage("line_splitter"));

                Optional<BankAccount> bankAccount = signUp.waitUntilBankAccountSelect(scanner, user);

                if (bankAccount.isPresent()) {
                    CommandController commandController = new CommandController(user, backend, bankAccount.get(), historyInputs);

                    while (user != null) {

                        System.out.println(localization.getMessage("line_splitter"));
                        String input = scanner.nextLine();
                        historyInputs.add(input);

                        String stringOutput = commandController.handleInput(input);
                        System.out.println(stringOutput);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a new user with name "TestUser" and password "passwort1234"
    private static void createTestUsers(UserService userService) {
        try {
            userService.createUser(User.createUser("TestUser",
                    fromPlainText("passwort1234")));

            userService.createUser(User.createUser("TestUser2",
                    fromPlainText("passwort5678")));
        } catch (DatabaseException e) {
            System.out.println("Error creating test users");
        }
    }
}