package org.bilanzius.commands.implementations;

import org.bilanzius.persistence.models.BankAccount;

public interface BankAccountAware
{
    void setSelectedBankAccount(BankAccount bankAccount);
}
