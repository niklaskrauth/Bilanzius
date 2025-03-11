package org.bilanzius.persistence.sql;

import java.io.File;

public class SqlBackendUtils {

    private static final String FILE_PATH = "target/test.sqlite";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + FILE_PATH;

    public static void deleteTestDatabase() {
        var file = new File(FILE_PATH);

        if (file.exists() && !file.delete()) {
            throw new IllegalStateException("Can't delete sqlite file");
        }
    }
}
