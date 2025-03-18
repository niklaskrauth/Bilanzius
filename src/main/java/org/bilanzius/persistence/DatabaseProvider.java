package org.bilanzius.persistence;

public class DatabaseProvider
{

    private static DatabaseServiceRepository repository;

    public static void init(DatabaseServiceRepository repository)
    {
        if (DatabaseProvider.repository != null) {
            throw new IllegalStateException("DatabaseProvider was already initialized.");
        }

        DatabaseProvider.repository = repository;
    }

    public static UserService getUserService()
    {
        return DatabaseProvider.repository.getUserService();
    }

    public static TransactionService getTransactionService()
    {
        return DatabaseProvider.repository.getTransactionService();
    }

    public static BankAccountService getBankAccountService()
    {
        return DatabaseProvider.repository.getBankAccountService();
    }

    public static CategoryService getCategoryService()
    {
        return DatabaseProvider.repository.getCategoryService();
    }

    private DatabaseProvider()
    {
    }
}
