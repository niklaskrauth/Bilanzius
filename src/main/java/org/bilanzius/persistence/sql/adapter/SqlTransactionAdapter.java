package org.bilanzius.persistence.sql.adapter;

import org.bilanzius.persistence.models.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlTransactionAdapter implements SqlDataAdapter<Transaction> {

    @Override
    public Transaction deserialize(ResultSet resultSet) throws SQLException {
        return new Transaction(resultSet.getInt("id"), resultSet.getInt("userId"),
                resultSet.getInt("accountId"), resultSet.getInt("categoryId"),
                resultSet.getBigDecimal("money"), resultSet.getString("description"));
    }
}
