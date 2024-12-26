package org.bilanzius.persistence.models;

public class BankAccount {

    public static BankAccount create(User user, String name) {
        return new BankAccount(
                0, user.getId(), name, 0.0
        );
    }

    private final int accountId;
    private final int userId;
    private String name;
    private final Double balance;

    public BankAccount(int accountId, int userId, String name, Double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }


    @Override
    public String toString() {
        return "Account{ " +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
