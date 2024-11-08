package org.bilanzius.persistence.sql.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ein Data Converter verwaltet die Transformation von einem SQL ResultSet zu einer Modellklasse und umgekehrt.
 *
 * @param <T> Die Modellklasse, die von diesem Converter verwaltet wird.
 */
public interface SqlDataAdapter<T> {

    T deserialize(ResultSet resultSet) throws SQLException;

}
