package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Prodotti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {

    public ProdottiController(){
    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"prodotti");
    }

    @Override
    public Optional<Prodotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"prodotti",new Prodotti());
    }

    @Override
    public List<Prodotti> getAllObjects() {
        return Database.getAllElements("prodotti",new Prodotti());
    }
}
