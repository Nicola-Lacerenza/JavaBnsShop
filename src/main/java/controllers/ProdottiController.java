package controllers;

import models.Prodotti;
import utility.Database;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {

    public ProdottiController(){

    }

    @Override
    public Optional<Prodotti> insertObject(Map<String, String> request) {
        Prodotti pC = Database.insertElement(request,"prodotti",new Prodotti());
        if (pC==null){
            return Optional.empty();
        }
        return Optional.of(pC);
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
