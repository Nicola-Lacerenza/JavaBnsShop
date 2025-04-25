package controllers;

import models.Modello;
import utility.Database;
import utility.QueryFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloController implements Controllers<Modello> {

    public ModelloController(){

    }

    @Override
    public int insertObject(Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        return Database.insertElement(request,"modello");
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
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
    public List<Modello> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
