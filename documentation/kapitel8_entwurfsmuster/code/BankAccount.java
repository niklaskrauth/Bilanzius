package org.bilanzius.persistence.models;

import java.math.BigDecimal;

public class BankAccount {

    private long accountId;
    private long userId;
    private String name;
    private BigDecimal balance;

    private BankAccount(long accountId, long userId, String name, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public static BankAccount create(User user, String name) {
        return new BankAccount(0, user.getId(), name, new BigDecimal("0.0"));
    }

    //Getter & Setter
    //...
}