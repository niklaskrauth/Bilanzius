package org.bilanzius.persistence;

import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.HashedPassword;

import java.util.Optional;

public interface UserDatabaseService {

    void createUser(User user);

    Optional<User> findUser(long id);

    Optional<User> findUserWithCredentials(String username, HashedPassword password);

    void updateUser(User user);

    void updateUserBalance(User user);
}
