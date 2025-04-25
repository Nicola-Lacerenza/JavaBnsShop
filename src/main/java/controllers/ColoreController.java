package controllers;

import models.Colore;
import utility.Database;
import utility.QueryFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreController implements Controllers<Colore> {
    public ColoreController(){

    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return Database.insertElement(request,"colore");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return Database.updateElement(id,request, "colore");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"colore");
    }

    @Override
    public Optional<Colore> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"colore",new Colore());
    }

    @Override
    public List<Colore> getAllObjects() {
        return Database.getAllElements("colore",new Colore());

    }

    @Override
    public List<Colore> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
