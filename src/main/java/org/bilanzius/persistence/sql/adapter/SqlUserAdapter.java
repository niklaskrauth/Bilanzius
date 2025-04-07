package org.bilanzius.persistence.sql.adapter;

import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.HashedPassword;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserAdapter implements SqlDataAdapter<User>
{

    @Override
    public User deserialize(ResultSet resultSet) throws SQLException
    {
        var id = resultSet.getInt("id");
        var userName = resultSet.getString("user");
        var password = resultSet.getString("password");
        var mainBankAccountId = resultSet.getInt("mainBankAccountId");

        return new User(id, userName, HashedPassword.fromHashedText(password), mainBankAccountId
        );
    }

}
