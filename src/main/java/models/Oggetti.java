package models;

import java.sql.ResultSet;
import java.util.Optional;

public interface Oggetti <E> {

    E createObject();

    Optional<E> convertDBToJava(ResultSet rs);
}
