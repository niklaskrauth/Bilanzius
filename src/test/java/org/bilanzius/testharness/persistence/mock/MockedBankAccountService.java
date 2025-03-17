package org.bilanzius.testharness.persistence.mock;

import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.testharness.persistence.ModelUtils;

import java.util.List;
import java.util.Optional;

public class MockedBankAccountService implements BankAccountService {

    @Override
    public void createBankAccount(BankAccount bankAccount) {
        // ignored
    }

    @Override
    public Optional<BankAccount> getBankAccount(long id) {
        return Optional.of(ModelUtils.existingBankAccount());
    }

    @Override
    public Optional<BankAccount> getBankAccountOfUserByName(User user, String name) {
        return Optional.empty();
    }

    @Override
    public List<BankAccount> getBankAccountsOfUser(User user, int limit) {
        return List.of();
    }

    @Override
    public void updateBankAccount(BankAccount bankAccount) {
        // ignored
    }

    @Override
    public void deleteBankAccount(BankAccount bankAccount) {
        // ignored
    }
}
