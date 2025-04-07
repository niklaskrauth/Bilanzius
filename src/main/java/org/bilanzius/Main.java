package org.bilanzius;

import org.bilanzius.cli.CLIContext;
import org.bilanzius.cli.IOContext;
import org.bilanzius.commandController.CommandController;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqlDatabaseServiceRepository;
import org.bilanzius.rest.MainRestController;
import org.bilanzius.utils.Localization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class Main
{

    public static final int MAX_BANK_ACCOUNTS = 3;

    public static void main(String[] args)
    {
        try {
            bootstrap();
        } catch (
            SQLException | RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    private static void bootstrap() throws SQLException
    {
        // Connect to
        // sqllite database
        setupDatabase();

        SignUp signUp = new SignUp();
        Scanner scanner = new Scanner(System.in);
        IOContext context = new CLIContext(scanner, Localization.getInstance());
        List<String> historyInputs = new ArrayList<>();
        MainRestController mainRestController = new MainRestController();
        User user;


        createTestUsers(DatabaseProvider.getUserService());
        WelcomeUser.welcomeMessage();

        mainRestController.start();

        while (true) {

            user = signUp.waitUntilLoggedIn(context);
            context.lineSeperator();

            Optional<BankAccount> bankAccount = signUp.waitUntilBankAccountSelect(scanner, user);

            if (bankAccount.isPresent()) {
                CommandController commandController = new CommandController(user, bankAccount.get(), historyInputs);

                while (user != null) {
                    context.lineSeperator();
                    String input = scanner.nextLine();
                    historyInputs.add(input);

                    String stringOutput = commandController.handleInput(input);
                    System.out.println(stringOutput);
                }
            }
        }
    }

    private static void setupDatabase() throws SQLException
    {
        var backend = new SqlBackend();
        backend.connect();
        DatabaseProvider.init(new SqlDatabaseServiceRepository(backend));
        createTestUsers(DatabaseProvider.getUserService());
    }

    // Create a new user with Name "TestUser" and password "passwort1234"
    // and a second user with Name "TestUser2" and password "passwort5678"
    private static void createTestUsers(UserService userService)
    {
        try {
            userService.createUser(User.createUser("TestUser",
                fromPlainText("passwort1234")));

            userService.createUser(User.createUser("TestUser2",
                fromPlainText("passwort5678")));
        } catch (
            DatabaseException e) {
            System.out.println("Error creating test users");
        }
    }
}