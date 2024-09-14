package controllers;

import models.Fornitori;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriController implements Controllers<Fornitori> {

    public FornitoriController(){
    }

    @Override
    public boolean insertObject(Map<String, String> request) {
        return Database.insertElement(request,"fornitori");
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
    public Optional<Fornitori> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Fornitori> getAllObjects() {
        return null;
    }
}
