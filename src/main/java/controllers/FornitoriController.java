package controllers;

import models.Fornitori;
import models.FornitoriProdotti;
import utility.Database;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriController implements Controllers<Fornitori> {

    public FornitoriController(){
    }

    @Override
    public Optional<Fornitori> insertObject(Map<String, String> request) {
        Fornitori f = Database.insertElement(request,"fornitori",new Fornitori());
        if (f==null){
            return Optional.empty();
        }
        return Optional.of(f);
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
