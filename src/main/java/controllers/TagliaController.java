package controllers;

import models.Taglia;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TagliaController implements Controllers<Taglia> {

    public TagliaController(){

    }

    @Override
    public Optional<Taglia> insertObject(Map<String, String> request) {
        return Optional.empty();
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
    public Optional<Taglia> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Taglia> getAllObjects() {
        return null;
    }
}
