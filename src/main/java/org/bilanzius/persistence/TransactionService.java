package org.bilanzius.persistence;

import org.bilanzius.persistence.models.Transaction;
import org.bilanzius.persistence.models.User;

import java.util.List;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    List<Transaction> getTransactions(User user, int limit, int skip);
}