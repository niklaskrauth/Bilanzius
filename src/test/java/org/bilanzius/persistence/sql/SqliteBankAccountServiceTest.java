package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.testharness.persistence.ModelUtils;
import org.bilanzius.testharness.persistence.SqlTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

class SqliteBankAccountServiceTest extends SqlTest
{

    private static final String NAME = "account";

    @Test
    void testCreateAndGet()
    {
        var service = bankAccountService();

        service.createBankAccount(BankAccount.create(ModelUtils.existingUser(), NAME));
        var account = service.getBankAccount(1).orElseThrow();

        Assertions.assertEquals(NAME, account.getName());
        Assertions.assertFalse(service.getBankAccountsOfUser(ModelUtils.existingUser(), 1).isEmpty());
        Assertions.assertFalse(service.getBankAccountOfUserByName(ModelUtils.existingUser(), NAME).isEmpty());
    }

    @Test
    void testUpdate()
    {
        var service = bankAccountService();

        service.createBankAccount(BankAccount.create(ModelUtils.existingUser(), NAME));
        var account = service.getBankAccount(1).orElseThrow();
        account.setName("new_name");
        account.setBalance(BigDecimal.TWO);
        service.updateBankAccount(account);

        var newAccount = service.getBankAccount(1).orElseThrow();
        Assertions.assertEquals("new_name", newAccount.getName());
        Assertions.assertEquals(2.0D, newAccount.getBalance().doubleValue());
    }

    @Test
    void testDelete()
    {
        var service = bankAccountService();

        service.createBankAccount(BankAccount.create(ModelUtils.existingUser(), NAME));
        service.deleteBankAccount(service.getBankAccount(1).orElseThrow());

        Assertions.assertTrue(service.getBankAccount(1).isEmpty());
    }

    @Test
    void testDatabaseExceptionIfNoDatabaseConnection() throws SQLException
    {
        // Simulate connection lost
        var service = bankAccountService();
        this.sqlBackend.close();

        Assertions.assertThrows(DatabaseException.class, () -> service.createBankAccount(ModelUtils.existingBankAccount()));
        Assertions.assertThrows(DatabaseException.class, () -> service.updateBankAccount(ModelUtils.existingBankAccount()));
        Assertions.assertThrows(DatabaseException.class, () -> service.getBankAccount(0));
        Assertions.assertThrows(DatabaseException.class, () -> service.getBankAccountsOfUser(ModelUtils.existingUser(), 1));
        Assertions.assertThrows(DatabaseException.class, () -> service.getBankAccountOfUserByName(ModelUtils.existingUser(), ""));
        Assertions.assertThrows(DatabaseException.class, () -> service.deleteBankAccount(ModelUtils.existingBankAccount()));
    }

    private BankAccountService bankAccountService()
    {
        try
            {
                var backend = requestBackend();
                return new SqliteBankAccountService(backend);
            } catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
    }
}
