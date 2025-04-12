package org.bilanzius.persistence;

public interface DatabaseService
{

    UserService getUserService();

    TransactionService getTransactionService();

    BankAccountService getBankAccountService();

    CategoryService getCategoryService();
}
