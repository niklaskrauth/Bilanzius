package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.sql.*;
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
        BankAccountService bankAccountService;

        try {
            backend.connect();
            userService = SqliteUserDatabaseService.getInstance(backend);
            transactionService = SqliteTransactionService.getInstance(backend);
            categoryService = SqliteCategoryService.getInstance(backend);
            bankAccountService = SqliteBankAccountService.getInstance(backend);

            Localization localization = Localization.getInstance();

            // Create a new user with name "TestUser" and password "passwort1234"
            userService.createUser(User.createUser("TestUser",
                    fromPlainText("passwort1234")));

            userService.createUser(User.createUser("TestUser2",
                    fromPlainText("passwort5678")));

            SignUp signUp = new SignUp(userService, bankAccountService);

            System.out.println(localization.getMessage("greeting"));

            Scanner input = new Scanner(System.in);

            while (true) {

               User user = signUp.waitUntilLoggedIn(input); //TODO: Implement Register @Niklas

               BankAccount bankAccount = signUp.waitUntilBankAccountSelect(input, user);

               while (user != null) {

                   System.out.println("----------------------------------------------------------------------------------");

                   String stringInput = input.nextLine();

                   CommandController commandController = new CommandController(user, userService, bankAccountService,
                           categoryService, transactionService, bankAccount);

                   String stringOutput = commandController.handleInput(stringInput);
                   System.out.println(stringOutput);

               }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}