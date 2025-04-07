package org.bilanzius.persistence.sql.adapter;

import org.bilanzius.persistence.models.BankAccount;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlBankAccountAdapter implements SqlDataAdapter<BankAccount>
{
    @Override
    public BankAccount deserialize(ResultSet resultSet) throws SQLException
    {
        return new BankAccount(
            resultSet.getInt("id"),
            resultSet.getInt("userId"),
            resultSet.getString("name"),
            resultSet.getBigDecimal("balance")
        );
    }
}
