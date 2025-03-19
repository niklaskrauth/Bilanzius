package org.bilanzius.persistence;

public interface DatabaseServiceRepository
{

    UserService getUserService();

    TransactionService getTransactionService();

    BankAccountService getBankAccountService();

    CategoryService getCategoryService();
}
