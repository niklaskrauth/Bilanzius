package org.bilanzius.rest.consumer;

import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.dto.BankAccountDTO;

import java.io.IOException;

@FunctionalInterface
public interface BankAccountRestConsumer
{

    void accept(User user, BankAccount bankAccount, BankAccountDTO bankAccountDTO) throws IOException;

}
