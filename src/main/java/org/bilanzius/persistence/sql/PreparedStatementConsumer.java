package org.bilanzius.persistence.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConsumer
{

    void accept(PreparedStatement statement) throws SQLException;
}
