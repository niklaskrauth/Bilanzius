package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.testharness.persistence.ModelUtils;
import org.bilanzius.testharness.persistence.SqlTest;
import org.bilanzius.testharness.persistence.mock.MockedBankAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;

class SqliteTransactionServiceTest extends SqlTest
{

    @Test
    void testCreateTransaction()
    {
        var service = transactionService();
        service.saveTransaction(new Transaction(1, 1, 1, 1, BigDecimal.ZERO, Instant.now(), "1234"));
        service.saveTransaction(new Transaction(1, 1, 1, -1, BigDecimal.ZERO, Instant.now(), "1234"));

        var transactions = service.getTransactions(ModelUtils.existingUser(), ModelUtils.existingBankAccount(), 10, 0);
        Assertions.assertEquals(2, transactions.size());
    }

    @Test
    void testCreateTransactionInvalidUser()
    {
        var service = transactionService();

        Assertions.assertThrows(DatabaseException.class, () -> service.saveTransaction(new Transaction(1, 5, 1, 1, BigDecimal.ZERO, Instant.now(), "1234")));
    }

    @Test
    void testDatabaseExceptionIfNoDatabaseConnection() throws SQLException
    {
        // Simulate connection lost
        var service = transactionService();
        this.sqlBackend.close();

        Assertions.assertThrows(DatabaseException.class, () -> service.saveTransaction(ModelUtils.transaction()));
        Assertions.assertThrows(DatabaseException.class, () -> service.getTransactions(ModelUtils.existingUser(), ModelUtils.existingBankAccount(), 10, 0));
    }

    private SqliteTransactionService transactionService()
    {
        try
            {
                var backend = requestBackend();
                return new SqliteTransactionService(backend, new MockedBankAccountService());
            } catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
    }
}
