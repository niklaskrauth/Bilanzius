package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.adapter.SqlUserAdapter;
import org.bilanzius.utils.HashedPassword;

import java.sql.SQLException;
import java.util.Optional;

public class SqliteUserDatabaseService implements UserDatabaseService {

    private final SqlBackend backend;

    public SqliteUserDatabaseService(SqlBackend backend) throws SQLException {
        this.backend = backend;
        this.backend.registerAdapter(User.class, new SqlUserAdapter());

        this.createSchema();
    }

    private void createSchema() throws SQLException {
        this.backend.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user TEXT,
                    password TEXT
                )
                """);
    }

    @Override
    public void createUser(User user) {
        try {
            backend.execute("INSERT INTO users (user, password) VALUES (?,?)", stmt -> {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getHashedPassword().getPassword());
            });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Optional<User> findUser(long id) {
        try {
            return backend.query(User.class, "SELECT * FROM users WHERE id = ?", stmt -> stmt.setLong(1, id))
                    .stream()
                    .findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Optional<User> findUserWithCredentials(String username, HashedPassword password) {
        try {
            return backend.query(User.class, "SELECT * FROM users WHERE user = ? AND password = ?", stmt -> {
                        stmt.setString(1, username);
                        stmt.setString(2, password.getPassword());
                    })
                    .stream()
                    .findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public void updateUser(User user) {
        if (!user.canBeUpdated()) {
            throw new DatabaseException("User can't be updated.");
        }

        try {
            backend.execute("UPDATE users SET password = ? WHERE id = ?", stmt -> {
                stmt.setString(1, user.getHashedPassword().getPassword());
                stmt.setInt(2, user.getId());
            });
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }
}