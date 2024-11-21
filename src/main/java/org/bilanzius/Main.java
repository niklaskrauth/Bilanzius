package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteCategoryService;
import org.bilanzius.persistence.sql.SqliteTransactionService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.HashedPassword;
import org.bilanzius.utils.Localization;
import org.bilanzius.persistence.models.User;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Connect to sqllite database
        var backend = new SqlBackend();
        UserDatabaseService userService = null;
        TransactionService transactionService = null;
        CategoryService categoryService = null;
        try {
            backend.connect();
            userService = new SqliteUserDatabaseService(backend);
            transactionService = new SqliteTransactionService(backend);
            categoryService = new SqliteCategoryService(backend);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Localization localization = Localization.getInstance();

        // Create a new user with name "TestUser" and password "passwort1234"
        assert userService != null;
        userService.createUser(User.createUser("TestUser",
                HashedPassword.fromPlainText("passwort1234")));

        // Find user with credentials
        var databaseUser = userService
                .findUserWithCredentials("TestUser", HashedPassword.fromPlainText("passwort1234"))
                .orElseThrow();

        System.out.println(localization.getMessage("greeting", databaseUser.getUsername()));
        Scanner input  = new Scanner(System.in);

        CommandController commandController = new CommandController(databaseUser, userService, categoryService,
                transactionService);

        while(true) {
            System.out.println("----------------------------------------------------------------------------------");

            String stringInput = input.nextLine();

            if (stringInput != null) {
                String stringOutput = commandController.handleInput(stringInput);
                System.out.println(stringOutput);
            }

        }

    }
}