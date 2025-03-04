package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.TransactionService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlTransactionAdapter;

import java.sql.SQLException;
import java.util.List;

public class SqliteTransactionService implements TransactionService {

    private static SqliteTransactionService instance;
    private final SqlBackend backend;
    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;


    private SqliteTransactionService(SqlBackend backend) throws SQLException {
        this.backend = backend;
        this.backend.registerAdapter(Transaction.class, new SqlTransactionAdapter());
        this.bankAccountService = SqliteBankAccountService.getInstance(backend);
        this.categoryService = SqliteCategoryService.getInstance(backend);

        this.createSchema();
    }

    public static synchronized SqliteTransactionService getInstance(SqlBackend backend) throws SQLException {
        if (instance == null) {
            instance = new SqliteTransactionService(backend);
        }
        return instance;
    }

    private void createSchema() throws SQLException {
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    accountId INTEGER,
                    categoryId INTEGER,
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
            Category category = transaction.getCategoryId() == -1 ? null : categoryService.getCategory(transaction.getCategoryId()).orElse(null);
            if (category != null) {
                category.setAmountSpent(category.getAmountSpent().subtract(transaction.getMoney()));
                categoryService.updateCategory(category);
            }

            BankAccount bankAccount = bankAccountService.getBankAccount(transaction.getAccountId()).orElseThrow();
            if (bankAccount.getUserId() != transaction.getUserId()) {
                throw new DatabaseException("Bank account does not belong to user");
            }
            bankAccount.setBalance(bankAccount.getBalance().add(transaction.getMoney()));
            bankAccountService.updateBankAccount(bankAccount);

            this.backend.execute("INSERT INTO transactions (userId, accountId, categoryId, description, money) VALUES (?,?,?,?,?)",
                    stmt -> {
                        stmt.setInt(1, transaction.getUserId());
                        stmt.setInt(2, transaction.getAccountId());
                        stmt.setObject(3, transaction.getCategoryId() == -1 ? null : transaction.getCategoryId(), java.sql.Types.INTEGER);
                        stmt.setString(4, transaction.getDescription());
                        stmt.setDouble(5, transaction.getMoney().doubleValue());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Transaction> getTransactions(User user, int limit, int skip) {
        //TODO Implement
        return List.of();
    }
}
