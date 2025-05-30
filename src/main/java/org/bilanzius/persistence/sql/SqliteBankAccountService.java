package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlBankAccountAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SqliteBankAccountService implements BankAccountService
{

    private final SqlBackend backend;

    SqliteBankAccountService(SqlBackend backend) throws SQLException
    {
        this.backend = backend;
        this.backend.registerAdapter(BankAccount.class, new SqlBankAccountAdapter());
        this.createSchema();
    }

    private void createSchema() throws SQLException
    {
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS bankAccounts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    name TEXT,
                    balance REAL DEFAULT 0.0,
                    FOREIGN KEY(userId) REFERENCES users(id),
                    UNIQUE(userId, name)
                )
                """);
    }

    @Override
    public void createBankAccount(BankAccount bankAccount)
    {
        try {
            backend.execute("INSERT INTO bankAccounts (userId, name) VALUES (?,?)",
                    stmt ->
                    {
                        stmt.setInt(1, bankAccount.getUserId());
                        stmt.setString(2, bankAccount.getName());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Optional<BankAccount> getBankAccount(long id)
    {
        try {
            return backend.query(BankAccount.class, "SELECT * FROM bankAccounts WHERE id = ?",
                    stmt -> stmt.setLong(1, id)).stream().findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Optional<BankAccount> getBankAccountOfUserByName(User user, String name)
    {
        try {
            return backend.query(BankAccount.class, "SELECT * FROM bankAccounts WHERE userId = ? AND name = ?",
                    stmt ->
                    {
                        stmt.setInt(1, user.getId());
                        stmt.setString(2, name);
                    }).stream().findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<BankAccount> getBankAccountsOfUser(User user, int limit)
    {
        try {
            Collection<BankAccount> bankAccounts = backend.query(BankAccount.class,
                    "SELECT * FROM bankAccounts WHERE userId = ? LIMIT ?",
                    stmt ->
                    {
                        stmt.setInt(1, user.getId());
                        stmt.setInt(2, limit);
                    });
            return new ArrayList<>(bankAccounts);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void updateBankAccount(BankAccount bankAccount)
    {
        try {
            backend.execute("UPDATE bankAccounts SET name = ?, balance = ? WHERE id = ?",
                    stmt ->
                    {
                        stmt.setString(1, bankAccount.getName());
                        stmt.setDouble(2, bankAccount.getBalance().doubleValue());
                        stmt.setInt(3, bankAccount.getAccountId());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void deleteBankAccount(BankAccount bankAccount)
    {
        try {
            backend.execute("DELETE FROM bankAccounts WHERE id = ?",
                    stmt -> stmt.setInt(1, bankAccount.getAccountId()));
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}
