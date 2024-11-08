package org.bilanzius.persistence.sql.test;

import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteTransactionService;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.utils.HashedPassword;

public class Test {

    public static void main(String[] args) throws Exception {
        // Connect to sqllite database
        var backend = new SqlBackend();
        backend.connect();

        // Setup
        UserDatabaseService userService = new SqliteUserDatabaseService(backend);
        TransactionService transactionService = new SqliteTransactionService(backend);

        // Create a new user with name "test2" and password "passwort123"
        userService.createUser(User.createUser("test2", HashedPassword.fromPlainText("passwort123")));

        // Find user with credentials
        var user = userService
                .findUserWithCredentials("test2", HashedPassword.fromPlainText("passwort123"))
                .orElseThrow();

        // create transaction
        transactionService.saveTransaction(Transaction.create(user, 5.00, "5€"));

        // Update password
        user.setHashedPassword(HashedPassword.fromPlainText("kuchen123"));
        userService.updateUser(user);
    }
}
