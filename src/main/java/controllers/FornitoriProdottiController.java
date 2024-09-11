package controllers;

import models.FornitoriProdotti;
import models.Immagini;
import utility.Database;


import java.util.*;

public class FornitoriProdottiController implements Controllers<FornitoriProdotti> {

    public FornitoriProdottiController(){

    }

    @Override
    public Optional<FornitoriProdotti> insertObject(Map<String, String> request) {
        FornitoriProdotti fP = Database.insertElement(request,"fornitoriProdotti",new FornitoriProdotti());
        if (fP==null){
            return Optional.empty();
        }
        return Optional.of(fP);
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
