package org.bilanzius.persistence.sql.adapter;

import org.bilanzius.persistence.models.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlCategoryAdapter implements SqlDataAdapter<Category> {

    @Override
    public Category deserialize(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getInt("id"),
                resultSet.getInt("userId"),
                resultSet.getString("name"),
                resultSet.getDouble("budget"),
                resultSet.getDouble("amountSpent")
        );
    }
}
