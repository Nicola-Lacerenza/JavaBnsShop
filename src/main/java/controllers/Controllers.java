package controllers;

import java.util.Map;

public interface Controllers<E> {
    E insertObject(Map<String,String> request);

    boolean updateObject(Map<String,String> request);

    boolean deleteObject(Map<String,String> request);

    String  getObject(Map<String,String> request);

    String getAllObjects(Map<String,String> request);



}
