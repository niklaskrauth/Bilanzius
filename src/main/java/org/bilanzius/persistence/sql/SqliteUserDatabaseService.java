package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.UserDatabaseService;
import org.bilanzius.persistence.models.User;

import java.sql.SQLException;
import java.util.Optional;

public class SqliteUserDatabaseService implements UserDatabaseService {

    private final SqlBackend backend;

    public SqliteUserDatabaseService(SqlBackend backend) throws SQLException {
        this.backend = backend;
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
    public void createUser(User user) throws DatabaseException {
        try {
            backend.execute("INSERT INTO users (user, password) VALUES (?,?)", stmt -> {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getHashedPassword());
            });
        } catch (SQLException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    public Optional<User> findUser(long id) throws DatabaseException {
        try {
            return backend.query(User.class, "SELECT * FROM users WHERE id = ?", stmt -> stmt.setLong(1, id))
                    .stream()
                    .findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    public Optional<User> findUserWithCredentials(String username, String password) throws DatabaseException {
        try {
            return backend.query(User.class, "SELECT * FROM users WHERE user = ? AND password = ?", stmt -> {
                        stmt.setString(1, username);
                        stmt.setString(2, password);
                    })
                    .stream()
                    .findFirst();
        } catch (SQLException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException();
    }
}
