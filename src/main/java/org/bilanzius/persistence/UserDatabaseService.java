package org.bilanzius.persistence;

import org.bilanzius.persistence.models.User;

import java.util.Optional;

public interface UserDatabaseService {

    void createUser(User user) throws DatabaseException;

    Optional<User> findUser(long id) throws DatabaseException;

    Optional<User> findUserWithCredentials(String username, String password) throws DatabaseException;

    void updateUser(User user) throws DatabaseException;
}
