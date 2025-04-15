package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.FornitoriProdotti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriProdottiController implements Controllers<FornitoriProdotti> {

    public FornitoriProdottiController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"fornitori_has_prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "fornitori_has_prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"fornitori_has_prodotti");
    }

    @Override
    public Optional<FornitoriProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"fornitori_has_prodotti",new FornitoriProdotti());
    }

    @Override
    public List<FornitoriProdotti> getAllObjects() {
        return Database.getAllElements("fornitori_has_prodotti",new FornitoriProdotti());
    }

    @Override
    public List<FornitoriProdotti> executeQuery(String query) {
        return null;
    }
}
