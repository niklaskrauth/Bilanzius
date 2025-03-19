package org.bilanzius.services.commands;

import org.bilanzius.persistence.models.BankAccount;

public interface BankAccountAware
{
    void setSelectedBankAccount(BankAccount bankAccount);
}
