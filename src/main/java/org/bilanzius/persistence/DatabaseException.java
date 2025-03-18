package org.bilanzius.persistence;

public class DatabaseException extends RuntimeException
{

    public DatabaseException(Exception ex)
    {
        super(ex);
    }

    public DatabaseException(String message)
    {
        super(message);
    }

    public DatabaseException()
    {
    }
}
