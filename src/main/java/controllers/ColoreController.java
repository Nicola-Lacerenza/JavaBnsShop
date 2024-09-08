package controllers;

import models.Colore;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreController implements Controllers<Colore> {
    public ColoreController(){

    }

    @Override
    public Optional<Colore> insertObject(Map<String, String> request) {
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
    public Optional<Colore> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Colore> getAllObjects() {
        return null;
    }
}
