package org.bilanzius.persistence.sql.test;

import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.persistence.sql.adapter.SqlUserAdapter;
import org.bilanzius.utils.HashedPassword;

public class Test {

    public static void main(String[] args) throws Exception {
        var backend = new SqlBackend();
        backend.registerAdapter(User.class, new SqlUserAdapter());
        backend.connect();

        var service = new SqliteUserDatabaseService(backend);
        service.createUser(User.createUser("test2", HashedPassword.fromPlainText("passwort123")));

        var user = service.findUserWithCredentials("test2", HashedPassword.fromPlainText("kuchen123")).orElseThrow();
        user.setHashedPassword(HashedPassword.fromPlainText("kuchen123"));
        service.updateUser(user);
        System.out.println(user);
    }
}
