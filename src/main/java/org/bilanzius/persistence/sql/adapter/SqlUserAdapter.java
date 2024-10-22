package org.bilanzius.persistence.sql.adapter;

import org.bilanzius.persistence.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserAdapter implements SqlDataAdapter<User> {

    @Override
    public User deserialize(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("id");
        var userName = resultSet.getString("userName");
        var password = resultSet.getString("hashedPassword");

        return new User(
                id, userName, password
        );
    }

}
