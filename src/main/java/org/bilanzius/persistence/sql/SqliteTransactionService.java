package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlTransactionAdapter;

import java.sql.SQLException;
import java.util.List;

public class SqliteTransactionService implements TransactionService
{

    private final SqlBackend backend;
    private final BankAccountService bankAccountService;

    SqliteTransactionService(SqlBackend backend, BankAccountService bankAccountService) throws SQLException
    {
        this.backend =
                backend;
        this.backend.registerAdapter(Transaction.class, new SqlTransactionAdapter());
        this.bankAccountService = bankAccountService;

        this.createSchema();
    }

    private void createSchema() throws SQLException
    {
        // SQLLite does not
        // have a time data
        // type, so we need
        // text instead.
        // https://www.sqlite.org/datatype3.html#date_and_time_datatype
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    accountId INTEGER,
                    categoryId INTEGER,
                    created TEXT,
                    description TEXT,
                    money REAL(2),
                    FOREIGN KEY(userId) REFERENCES users(id)
                )
                """);

    }

    @Override
    public void saveTransaction(Transaction transaction)
    {
        try {
            BankAccount bankAccount = bankAccountService.getBankAccount(transaction.getAccountId()).orElseThrow();
            if (bankAccount.getUserId() != transaction.getUserId()) {
                throw new DatabaseException("Bank account does not belong to user");
            }
            bankAccount.setBalance(bankAccount.getBalance().add(transaction.getMoney()));
            bankAccountService.updateBankAccount(bankAccount);

            this.backend.execute("INSERT INTO transactions (userId, accountId, categoryId,created, description, " +
                            "money) VALUES (?,?,?,?,?,?)",
                    stmt ->
                    {
                        stmt.setInt(1, transaction.getUserId());
                        stmt.setInt(2, transaction.getAccountId());
                        stmt.setObject(3, transaction.getCategoryId() == -1 ? null : transaction.getCategoryId(),
                                java.sql.Types.INTEGER);
                        stmt.setString(4, transaction.getCreated().toString());
                        stmt.setString(5, transaction.getDescription());
                        stmt.setDouble(6, transaction.getMoney().doubleValue());
                    });
        } catch (
                SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Transaction> getTransactions(User user, BankAccount account, int limit, int skip)
    {
        try {
            return this.backend.query(Transaction.class, "SELECT * FROM transactions WHERE userId=? AND accountId=? " +
                            "LIMIT ? OFFSET ?",
                    stmt ->
                    {
                        stmt.setInt(1, user.getId());
                        stmt.setInt(2, account.getAccountId());
                        stmt.setInt(3, limit);
                        stmt.setInt(4, skip);
                    }).stream().toList();
        } catch (
                SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
