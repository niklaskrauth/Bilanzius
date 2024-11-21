package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteCategoryService;
import org.bilanzius.persistence.sql.SqliteTransactionService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.Localization;
import org.bilanzius.persistence.models.User;

import java.sql.SQLException;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class Main {

    public static void main(String[] args) {
        // Connect to sqllite database
        var backend = new SqlBackend();
        UserService userService;
        TransactionService transactionService;
        CategoryService categoryService;

        try {
            backend.connect();
            userService = new SqliteUserDatabaseService(backend);
            transactionService = new SqliteTransactionService(backend);
            categoryService = new SqliteCategoryService(backend);

            Localization localization = Localization.getInstance();

            // Create a new user with name "TestUser" and password "passwort1234"
            userService.createUser(User.createUser("TestUser",
                    fromPlainText("passwort1234")));

            userService.createUser(User.createUser("TestUser2",
                    fromPlainText("passwort5678")));

            SingUp singUp = new SingUp(userService, localization);

            System.out.println(localization.getMessage("greeting"));

            Scanner input = new Scanner(System.in);

            while (true) {

               User user = singUp.login(input);

               while (user != null) {

                   System.out.println("----------------------------------------------------------------------------------");

                   String stringInput = input.nextLine();

                   CommandController commandController = new CommandController(user, userService, categoryService, transactionService);

                   String stringOutput = commandController.handleInput(stringInput);
                   System.out.println(stringOutput);

               }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}