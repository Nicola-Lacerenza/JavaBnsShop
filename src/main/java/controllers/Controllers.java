package controllers;

import bnsshop.bnsshop.RegisterServlet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Controllers<E> {
    boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request);

    boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request);

    boolean deleteObject(int objectid);

    Optional<E>  getObject(int objectid);

    List<E> getAllObjects();

    List<E> executeQuery(String query);

}
