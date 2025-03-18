package org.bilanzius.persistence.models;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction
{

    public static Transaction create(User user, BankAccount account, Category category, BigDecimal money, String description)
    {
        return create(user,
                account,
                category,
                money,
                Instant.now(), description);
    }

    public static Transaction create(User user, BankAccount account, BigDecimal money, String description)
    {
        return new Transaction(
                0,
                user.getId(), account.getAccountId(), -1, money, Instant.now(), description // -1 as default categoryId
        );
    }

    public static Transaction create(User user, BankAccount account, Category category, BigDecimal money, Instant created, String description)
    {
        return new Transaction(
                0,
                user.getId(), account.getAccountId(), category.getCategoryId(), money, created, description
        );
    }

    public static Transaction create(User user, BankAccount account, BigDecimal money, Instant created, String description)
    {
        return new Transaction(
                0,
                user.getId(), account.getAccountId(), -1, money, created, description // -1 as default categoryId
        );
    }

    private final int transactionId;
    private final int userId;
    private final int accountId;
    private final int categoryId;
    private final BigDecimal money;
    private final Instant created;

    private final String description;

    public Transaction(int transactionId, int userId, int accountId, int categoryId, BigDecimal money, Instant created, String description)
    {
        this.transactionId
                =
                transactionId;
        this.userId = userId;
        this.accountId =
                accountId;
        this.categoryId =
                categoryId;
        this.money = money;
        this.created =
                created;
        this.description =
                description;
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public BigDecimal getMoney()
    {
        return money;
    }

    public String getDescription()
    {
        return description;
    }

    public Instant getCreated()
    {
        return created;
    }

    @Override
    public String toString()
    {
        return "Transaction" +
                "{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", accountId=" + accountId +
                ", categoryId=" + categoryId +
                ", money=" + money +
                ", created" +
                "=" + created +
                ", description='" + description + '\'' +
                '}';
    }
}
