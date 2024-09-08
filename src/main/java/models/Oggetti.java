package models;

import java.sql.ResultSet;

public interface Oggetti <E> {

    E createObject();

    E convertDBToJava(ResultSet rs);
}
