package org.bilanzius.persistence.sql.test;

import org.bilanzius.persistence.models.User;
import org.bilanzius.persistence.sql.SqlBackend;
import org.bilanzius.persistence.sql.SqliteUserDatabaseService;
import org.bilanzius.persistence.sql.adapter.SqlUserAdapter;

public class Test {

    public static void main(String[] args) throws Exception {
        var backend = new SqlBackend();
        backend.registerAdapter(User.class, new SqlUserAdapter());
        backend.connect();

        var service = new SqliteUserDatabaseService(backend);
        service.createUser(User.createUser("test", "passwort123"));

        var user = service.findUser(1).orElseThrow();
        System.out.println(user);
    }
}