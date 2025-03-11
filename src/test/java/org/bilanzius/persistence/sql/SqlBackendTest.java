package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.testharness.persistence.SqlTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlBackendTest extends SqlTest {

    @Test
    void testMissingAdapter() {
        var backend = new SqlBackend();
        Assertions.assertThrows(DatabaseException.class, () -> backend.query(InvalidMode.class, "", c -> {
        }));
    }

    public record InvalidMode(int id) {
    }
}
