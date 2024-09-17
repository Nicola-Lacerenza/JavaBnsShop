package controllers;

import bnsshop.bnsshop.RegisterServlet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Controllers<E> {
    boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request);

    boolean updateObject(Map<String,String> request);

    boolean deleteObject(int objectid);

    Optional<E>  getObject(int objectid);

    List<E> getAllObjects();



}
