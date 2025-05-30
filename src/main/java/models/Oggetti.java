package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface Oggetti <E> {
    Optional<E> convertDBToJava(ResultSet rs) throws SQLException;
}
