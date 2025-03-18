package org.bilanzius.persistence;

import org.bilanzius.persistence.sql.SqlDatabaseServiceRepository;
import org.bilanzius.testharness.persistence.SqlTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DatabaseProviderTest extends SqlTest {

    @Test
    void testDatabaseProvider() throws SQLException {
        DatabaseProvider.init(new SqlDatabaseServiceRepository(requestBackend()));
        Assertions.assertNotNull(DatabaseProvider.getBankAccountService());
        Assertions.assertNotNull(DatabaseProvider.getUserService());
        Assertions.assertNotNull(DatabaseProvider.getTransactionService());
        Assertions.assertNotNull(DatabaseProvider.getCategoryService());

        Assertions.assertThrows(IllegalStateException.class, () -> DatabaseProvider.init(new SqlDatabaseServiceRepository(requestBackend())));
    }
}
