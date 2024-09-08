package controllers;

import models.FornitoriProdotti;


import java.util.*;

public class FornitoriProdottiController implements Controllers<FornitoriProdotti> {

    public FornitoriProdottiController(){

    }

    @Override
    public Optional<FornitoriProdotti> insertObject(Map<String, String> request) {
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
    public Optional<FornitoriProdotti> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<FornitoriProdotti> getAllObjects() {
        return null;
    }
}
