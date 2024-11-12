package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.CategoryService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.models.Category;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlCategoryAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SqliteCategoryService implements CategoryService {

    private final SqlBackend backend;

    public SqliteCategoryService(SqlBackend backend) throws SQLException {
        this.backend = backend;
        this.backend.registerAdapter(Category.class, new SqlCategoryAdapter());

        this.createSchema();
    }

    private void createSchema() throws SQLException {
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS categorys (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    name TEXT
                    budget REAL(2),
                    amountSpent REAL(2),
                    FOREIGN KEY(userId) REFERENCES users(id),
                    UNIQUE(userId, name)
                )
                """);
    }

    @Override
    public void createCategory(Category category){
        try {
            backend.execute("INSERT INTO categorys (userId, name, budget, amountSpent) VALUES (?,?,?,?)",
                    stmt -> {
                        stmt.setInt(1, category.getUserId());
                        stmt.setString(2, category.getName());
                        stmt.setDouble(3, category.getBudget());
                        stmt.setDouble(4, category.getAmountSpent());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Category> getCategoriesOfUser(User user, int limit) {
        try {
            Collection<Category> categories = backend.query(Category.class,
                    "SELECT * FROM categorys WHERE userId = ? LIMIT ?",
                    stmt -> {
                        stmt.setInt(1, user.getId());
                        stmt.setInt(2, limit);
                    });
            return new ArrayList<>(categories);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Optional<Category> getCategoriesOfUserByName(User user, String name) {
        try {
            return backend.query(Category.class, "SELECT * FROM categorys WHERE userId = ? AND name = ?",
                    stmt -> {
                        stmt.setInt(1, user.getId());
                        stmt.setString(2, name);
                    }).stream()
                    .findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Category> getExceededCategoriesOfUser(User user, int limit) {
        try {
            Collection<Category> categories = backend.query(Category.class,
                    "SELECT * FROM categorys WHERE userId = ? AND amountSpent > budget LIMIT ?",
                    stmt -> {
                        stmt.setInt(1, user.getId());
                        stmt.setInt(2, limit);
                    });
            return new ArrayList<>(categories);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void updateCategory(Category category) {
        try {
            backend.execute("UPDATE categorys SET name = ?, budget = ?, amountSpent = ? WHERE id = ?",
                    stmt -> {
                        stmt.setString(1, category.getName());
                        stmt.setDouble(2, category.getBudget());
                        stmt.setDouble(3, category.getAmountSpent());
                        stmt.setInt(4, category.getCategoryId());
                    });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

}
