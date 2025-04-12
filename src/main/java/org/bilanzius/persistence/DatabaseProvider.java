package org.bilanzius.persistence;

public class DatabaseProvider
{

    private static DatabaseService databaseService;

    public static void init(DatabaseService repository)
    {
        if (DatabaseProvider.databaseService != null) {
            throw new IllegalStateException("DatabaseProvider was already initialized.");
        }

        DatabaseProvider.databaseService = repository;
    }

    public static UserService getUserService()
    {
        return DatabaseProvider.databaseService.getUserService();
    }

    public static TransactionService getTransactionService()
    {
        return DatabaseProvider.databaseService.getTransactionService();
    }

    public static BankAccountService getBankAccountService()
    {
        return DatabaseProvider.databaseService.getBankAccountService();
    }

    public static CategoryService getCategoryService()
    {
        return DatabaseProvider.databaseService.getCategoryService();
    }

    private DatabaseProvider()
    {
    }
}
