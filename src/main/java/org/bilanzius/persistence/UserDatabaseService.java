package org.bilanzius.persistence;

import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.HashedPassword;

import java.util.Optional;

public interface UserDatabaseService {

    void createUser(User user) throws DatabaseException;

    Optional<User> findUser(long id) throws DatabaseException;

    Optional<User> findUserWithCredentials(String username, HashedPassword password) throws DatabaseException;

    void updateUser(User user) throws DatabaseException;
}
