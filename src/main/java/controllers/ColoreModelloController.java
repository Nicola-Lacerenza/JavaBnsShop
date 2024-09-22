package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.ColoreModello;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreModelloController implements Controllers<ColoreModello> {

    public ColoreModelloController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"colore_has_modello");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "colore_has_modello");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"colore_has_modello");
    }

    @Override
    public Optional<ColoreModello> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"colore_has_modello",new ColoreModello());
    }

    @Override
    public List<ColoreModello> getAllObjects() {
        return Database.getAllElements("colore_has_modello",new ColoreModello());
    }
}
