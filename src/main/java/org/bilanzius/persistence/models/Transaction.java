package org.bilanzius.persistence.models;

public class Transaction {

    public static Transaction create(User user, BankAccount account, Category category, double money, String description) {
        return new Transaction(
                0, user.getId(), account.getAccountId(), category.getCategoryId(), money, description
        );
    }

    public static Transaction create(User user, BankAccount account, double money, String description) {
        return new Transaction(
                0, user.getId(), account.getAccountId(), -1, money, description // -1 as default categoryId
        );
    }

    private final int transactionId;
    private final int userId;
    private final int accountId;
    private final int categoryId;
    private final double money;

    private final String description;

    public Transaction(int transactionId, int userId, int accountId, int categoryId, double money, String description) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.money = money;
        this.description = description;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }
}
