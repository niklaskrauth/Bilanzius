package org.bilanzius.persistence;

public class DatabaseException extends Exception {

    public DatabaseException(Exception ex) {
        super(ex);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException() {
    }
}
