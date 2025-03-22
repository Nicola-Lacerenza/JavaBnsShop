package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Fornitori;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriController implements Controllers<Fornitori> {

    public FornitoriController(){
    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"fornitori");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "fornitori");

    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"fornitori");
    }

    @Override
    public Optional<Fornitori> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"fornitori",new Fornitori());
    }

    @Override
    public List<Fornitori> getAllObjects() {
        return Database.getAllElements("fornitori",new Fornitori());
    }

    @Override
    public List<Fornitori> executeQuery(String query) {
        return null;
    }
}
