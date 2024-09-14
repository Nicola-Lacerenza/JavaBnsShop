package controllers;

import models.ColoreModello;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreModelloController implements Controllers<ColoreModello> {

    public ColoreModelloController(){

    }

    @Override
    public boolean insertObject(Map<String, String> request) {
        return Database.insertElement(request,"colore_has_modello");
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
    public Optional<ColoreModello> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<ColoreModello> getAllObjects() {
        return null;
    }
}
