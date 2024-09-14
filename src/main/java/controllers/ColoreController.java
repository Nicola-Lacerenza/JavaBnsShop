package controllers;

import models.Colore;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreController implements Controllers<Colore> {
    public ColoreController(){

    }

    @Override
    public boolean insertObject(Map<String, String> request) {
        return Database.insertElement(request,"colore");
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        return Database.updateElement(id,request, "colore");
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Colore> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Colore> getAllObjects() {
        return null;
    }
}
