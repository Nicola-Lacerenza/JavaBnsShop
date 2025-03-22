package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Taglia;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TagliaController implements Controllers<Taglia> {

    public TagliaController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"taglia");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "taglia");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"taglia");
    }

    @Override
    public Optional<Taglia> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"taglia",new Taglia());
    }

    @Override
    public List<Taglia> getAllObjects() {
        return Database.getAllElements("taglia",new Taglia());
    }

    @Override
    public List<Taglia> executeQuery(String query) {
        return null;
    }
}
