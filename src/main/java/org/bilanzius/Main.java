package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.HashedPassword;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Connect to sqllite database
        var backend = new SqlBackend();
        UserDatabaseService userService = null;
        try {
            backend.connect();
            userService = new SqliteUserDatabaseService(backend);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Localization localization = Localization.getInstance();

        // Create a new user with name "TestUser" and password "passwort1234"
        assert userService != null;
        userService.createUser(org.bilanzius.persistence.models.User.createUser("TestUser",
                HashedPassword.fromPlainText("passwort1234")));

        // Find user with credentials
        var databaseUser = userService
                .findUserWithCredentials("TestUser", HashedPassword.fromPlainText("passwort1234"))
                .orElseThrow();

        System.out.println(localization.getMessage("greeting", databaseUser.getUsername()));
        Scanner input  = new Scanner(System.in);

        User user = new User("User", 0);

        while(true) {

            System.out.println("----------------------------------------------------------------------------------");

            CommandController commandController = new CommandController(user);

            String stringInput = input.nextLine();

            if (stringInput != null) {
                String stringOutput = commandController.handleInput(stringInput);
                System.out.println(stringOutput);
            }

        }

    }
}