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
    private double balance;

    public BankAccount(int accountId, int userId, String name, double balance) {
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
