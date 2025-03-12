package org.bilanzius.testharness.persistence;

import org.bilanzius.persistence.sql.SqlBackend;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Parent class for all sqlite-based unit tests.
 * Ensures that the database is purged after each test to ensure consistent behaviour.
 */
public class SqlTest {

    private static final Logger LOGGER = Logger.getLogger(SqlTest.class.getSimpleName());
    private static final String FILE_PATH = "target/test.sqlite";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + FILE_PATH;

    protected SqlBackend sqlBackend = null;

    @BeforeEach
    public void beforeEach() {
        deleteTestDatabase();
    }

    @AfterEach
    public void afterEach() {
        disconnectBackend();
    }

    /**
     * @return The currently connected {@link SqlBackend} or a newly created object.
     */
    protected SqlBackend requestBackend() {
        if (this.sqlBackend != null) {
            return this.sqlBackend;
        }

        try {
            this.sqlBackend = new SqlBackend();
            this.sqlBackend.connect(CONNECTION_STRING);
            return this.sqlBackend;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * We need to disconnect the sql backend before deleting the sqlite file.
     */
    protected void disconnectBackend() {
        if (this.sqlBackend == null) {
            return;
        }

        try {
            this.sqlBackend.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            this.sqlBackend = null;
        }
    }

    private void deleteTestDatabase() {
        LOGGER.info("Deleting test database.");

        var file = new File("target", "test.sqlite");

        if (file.exists() && !file.delete()) {
            throw new IllegalStateException("Can't delete sqlite file");
        }
    }

}
