package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Modello;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloController implements Controllers<Modello> {

    public ModelloController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"modello");
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Modello> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Modello> getAllObjects() {
        return null;
    }
}
