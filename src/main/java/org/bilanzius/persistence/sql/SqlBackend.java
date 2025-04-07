package org.bilanzius.persistence.sql;

import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.sql.adapter.SqlDataAdapter;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SqlBackend
{

    private static final String SQLLITE_CONNECTION_STRING = "jdbc:sqlite:database.sqlite";

    private final Map<Class<?>, SqlDataAdapter<?>> adapters = new HashMap<>();
    private Connection connection;

    public void connect() throws SQLException
    {
        connect(SQLLITE_CONNECTION_STRING);
    }

    public void connect(String connectionString) throws SQLException
    {
        this.connection = DriverManager.getConnection(connectionString);
    }

    public void close() throws SQLException
    {
        this.connection.close();
    }

    public <T> void registerAdapter(Class<T> modelClass, SqlDataAdapter<T> adapter)
    {
        adapters.put(modelClass, adapter);
    }

    public <T> Collection<T> query(Class<T> modelClass, @Language("sql") String sqlCommand, PreparedStatementConsumer preparedStatementConsumer)
        throws SQLException, DatabaseException
    {
        var adapter = findAdapter(modelClass);

        try (var query = constructPreparedStatement(sqlCommand, preparedStatementConsumer))
            {
                var executedQuery = query.executeQuery();
                List<T> result = new ArrayList<>();

                while (executedQuery.next())
                    {
                        result.add(adapter.deserialize(executedQuery));
                    }

                return result;
            }
    }

    public void execute(@Language("sql") String sqlCommand) throws SQLException
    {
        execute(sqlCommand,
            stmt ->
            {
            });
    }

    public void execute(@Language("sql") String sqlCommand, PreparedStatementConsumer preparedStatementConsumer) throws SQLException
    {
        try (var statement = constructPreparedStatement(sqlCommand, preparedStatementConsumer))
            {
                statement.execute();
            }
    }

    private PreparedStatement constructPreparedStatement(@Language("sql") String sqlCommand,
                                                         PreparedStatementConsumer preparedStatementConsumer) throws SQLException
    {
        var query = connection.prepareStatement(sqlCommand);
        preparedStatementConsumer.accept(query);
        return query;
    }

    @SuppressWarnings(
        "unchecked")
    private <T> SqlDataAdapter<T> findAdapter(Class<T> modelClass) throws DatabaseException
    {
        var adapter = (SqlDataAdapter<T>) adapters.get(modelClass);

        if (adapter == null)
            {
                throw new DatabaseException();
            }

        return adapter;
    }
}
