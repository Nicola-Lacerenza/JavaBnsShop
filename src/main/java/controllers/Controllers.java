package controllers;

import utility.QueryFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Controllers<E> {
    int insertObject(Map<Integer,QueryFields<? extends Comparable<?>>> request);

    boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request);

    boolean deleteObject(int objectid);

    Optional<E>  getObject(int objectid);

    List<E> getAllObjects();

    List<E> executeQuery(String query, Map<Integer,QueryFields<? extends Comparable<?>>> fields);
}
