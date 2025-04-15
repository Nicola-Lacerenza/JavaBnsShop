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
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "modello");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"modello");
    }

    @Override
    public Optional<Modello> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"modello",new Modello());
    }

    @Override
    public List<Modello> getAllObjects() {
        return Database.getAllElements("modello",new Modello());
    }

    @Override
    public List<Modello> executeQuery(String query) {
        return null;
    }
}
