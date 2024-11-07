package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlTransactionAdapter;

import java.sql.SQLException;
import java.util.List;

public class SqliteTransactionService implements TransactionService {

    private final SqlBackend backend;

    public SqliteTransactionService(SqlBackend backend) throws SQLException {
        this.backend = backend;
        this.backend.registerAdapter(Transaction.class, new SqlTransactionAdapter());

        this.createSchema();
    }

    private void createSchema() throws SQLException {
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    created DATETIME DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ', 'now')),
                    description TEXT,
                    money REAL(2),
                    FOREIGN KEY(userId) REFERENCES users(id)
                )
                """);

    }

    @Override
    public void saveTransaction(Transaction transaction) {
        try {
            this.backend.execute("INSERT INTO transactions (userId, description, money) VALUES (?,?,?)",
                    stmt -> {
                        stmt.setInt(1, transaction.getUserId());
                        stmt.setString(2, transaction.getDescription());
                        stmt.setDouble(3, transaction.getMoney());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Transaction> getTransactions(User user, int limit, int skip) {
        return List.of();
    }
}
