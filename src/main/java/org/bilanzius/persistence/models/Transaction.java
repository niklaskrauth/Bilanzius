package org.bilanzius.persistence.models;

public class Transaction {

    public static Transaction create(User user, double money, String description) {
        return new Transaction(
                0, user.getId(), money, description
        );
    }

    private final int transactionId;
    private final int userId;
    private final double money;

    private final String description;

    public Transaction(int transactionId, int userId, double money, String description) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.money = money;
        this.description = description;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public double getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }
}
