package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.*;

import java.sql.SQLException;

public class SqlDatabaseServiceRepository implements DatabaseServiceRepository {

    private final UserService userService;
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;

    public SqlDatabaseServiceRepository(SqlBackend sqlBackend) throws SQLException {
        this.userService = new SqliteUserDatabaseService(sqlBackend);
        this.bankAccountService = new SqliteBankAccountService(sqlBackend);
        this.transactionService = new SqliteTransactionService(sqlBackend, this.bankAccountService);
        this.categoryService = new SqliteCategoryService(sqlBackend);
    }

    @Override
    public UserService getUserService() {
        return this.userService;
    }

    @Override
    public TransactionService getTransactionService() {
        return this.transactionService;
    }

    @Override
    public BankAccountService getBankAccountService() {
        return this.bankAccountService;
    }

    @Override
    public CategoryService getCategoryService() {
        return this.categoryService;
    }

}
