package org.bilanzius.testharness.persistence;

import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.HashedPassword;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Provides functions to create users.
 */
public class ModelUtils {

    public static User existingUser() {
        return new User(1, "user", HashedPassword.fromPlainText("password"), 1);
    }

    public static BankAccount existingBankAccount() {
        return new BankAccount(1, 1, "account", BigDecimal.ZERO);
    }

    public static Transaction transaction() {
        return new Transaction(1, 1, 1, 1, BigDecimal.ZERO, Instant.now(), "");
    }

    private ModelUtils() {
    }
}
