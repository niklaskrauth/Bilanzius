package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.User;
import org.bilanzius.testharness.persistence.SqlTest;
import org.bilanzius.utils.HashedPassword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SqliteUserDatabaseServiceTest extends SqlTest {

    private static final String USERNAME = "test";
    private static final HashedPassword DEFAULT_PASSWORD = HashedPassword.fromPlainText("123");
    private static final HashedPassword OTHER_PASSWORD = HashedPassword.fromPlainText("password");

    @Test
    void testDatabaseExceptionIfNoDatabaseConnection() throws SQLException {
        // Simulate connection lost
        var service = userService();
        this.sqlBackend.close();

        Assertions.assertThrows(DatabaseException.class, () -> service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD)));
        Assertions.assertThrows(DatabaseException.class, () -> service.updateUserMainAccountId(new User(1, USERNAME, DEFAULT_PASSWORD, 0)));
        Assertions.assertThrows(DatabaseException.class, () -> service.updateUserPassword(new User(1, USERNAME, DEFAULT_PASSWORD, 0)));
        Assertions.assertThrows(DatabaseException.class, () -> service.findUserWithName(USERNAME));
        Assertions.assertThrows(DatabaseException.class, () -> service.findUserWithCredentials(USERNAME, DEFAULT_PASSWORD));
        Assertions.assertThrows(DatabaseException.class, () -> service.findUser(1));
    }

    @Test
    void testFindUserByName() {
        // Setup test
        var service = userService();
        service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD));

        // Find user and validate
        var result = service.findUserWithName(USERNAME).orElseThrow();

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(USERNAME, result.getUsername());
        Assertions.assertEquals(DEFAULT_PASSWORD, result.getHashedPassword());
    }

    @Test
    void testFindUserWithCredentials() {
        var service = userService();
        service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD));

        Assertions.assertTrue(service.findUserWithCredentials(USERNAME, OTHER_PASSWORD).isEmpty());
        var result = service.findUserWithCredentials(USERNAME, DEFAULT_PASSWORD).orElseThrow();

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(USERNAME, result.getUsername());
        Assertions.assertEquals(DEFAULT_PASSWORD, result.getHashedPassword());
    }

    @Test
    void testFindUserById() {
        // Setup test
        var service = userService();
        service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD));

        // Find user and validate
        var result = service.findUser(1).orElseThrow();

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(USERNAME, result.getUsername());
        Assertions.assertEquals(DEFAULT_PASSWORD, result.getHashedPassword());
    }

    @Test
    void testUpdateUserPassword() {
        // Setup test
        var service = userService();
        var creatingUser = User.createUser(USERNAME, DEFAULT_PASSWORD);

        // user can't be updated before creation
        Assertions.assertFalse(creatingUser.canBeUpdated());

        service.createUser(creatingUser);

        // Find user and update password
        var user = service.findUser(1).orElseThrow();
        Assertions.assertTrue(user.canBeUpdated());
        user.setHashedPassword(OTHER_PASSWORD);
        service.updateUserPassword(user);

        // Find user again
        var sameUser = service.findUser(1).orElseThrow();
        Assertions.assertEquals(OTHER_PASSWORD, sameUser.getHashedPassword());
    }

    @Test
    void testUpdateUserMainAccount() {
        // Setup test
        var service = userService();
        service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD));

        // Find user and update password
        var user = service.findUser(1).orElseThrow();
        Assertions.assertTrue(user.canBeUpdated());
        user.setMainAccountId(42);
        service.updateUserMainAccountId(user);

        // Find user again
        var sameUser = service.findUser(1).orElseThrow();
        Assertions.assertEquals(42, sameUser.getMainBankAccountId());
    }

    private SqliteUserDatabaseService userService() {
        try {
            var backend = requestBackend();
            return new SqliteUserDatabaseService(backend);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
