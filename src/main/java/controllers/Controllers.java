package controllers;

import java.util.Map;
import java.util.Optional;

public interface Controllers<E> {
    Optional<E> insertObject(Map<String,String> request);

    boolean updateObject(Map<String,String> request);

    boolean deleteObject(Map<String,String> request);

    String  getObject(Map<String,String> request);

    String getAllObjects(Map<String,String> request);



}
