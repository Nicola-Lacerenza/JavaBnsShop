package controllers;

import models.Prodotti;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {

    public ProdottiController(){

    }

    @Override
    public Optional<Prodotti> insertObject(Map<String, String> request) {
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
    public Optional<Prodotti> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Prodotti> getAllObjects() {
        return null;
    }
}
