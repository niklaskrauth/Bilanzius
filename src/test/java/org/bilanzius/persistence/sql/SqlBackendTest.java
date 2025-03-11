package org.bilanzius.persistence.sql;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SqlBackendTest {

    @Test
    public void testBackend() throws SQLException {
        var setupDb = new SqlBackend();
        setupDb.connect(SqlBackendUtils.CONNECTION_STRING);
    }
}
