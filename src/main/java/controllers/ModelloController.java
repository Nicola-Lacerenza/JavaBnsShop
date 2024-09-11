package controllers;

import models.Modello;
import models.Prodotti;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloController implements Controllers<Modello> {

    public ModelloController(){

    }

    @Override
    public Optional<Modello> insertObject(Map<String, String> request) {
        Modello m = Database.insertElement(request,"modello",new Modello());
        if (m==null){
            return Optional.empty();
        }
        return Optional.of(m);
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
