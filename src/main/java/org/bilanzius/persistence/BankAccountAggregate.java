package org.bilanzius.persistence;

import org.bilanzius.persistence.models.BankAccount;

import java.math.BigDecimal;

public class BankAccountAggregate
{

    public static BankAccountAggregate fromEntity(BankAccount entity)
    {
        return new BankAccountAggregate(entity);
    }

    private final BankAccount entity;

    private BankAccountAggregate(BankAccount entity)
    {
        this.entity = entity;
    }

    public int getAccountId()
    {
        return entity.getAccountId();
    }

    public String getName()
    {
        return entity.getName();
    }

    public BigDecimal getBalance()
    {
        return this.entity.getBalance();
    }

    public void deposit(BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit must be positive");
        }

        this.entity.setBalance(this.getBalance().add(amount));
    }

    public void withdraw(BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Withdrawal must be positive");

        if (amount.compareTo(this.getBalance()) > 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        this.entity.setBalance(this.getBalance().subtract(amount));
    }
}
