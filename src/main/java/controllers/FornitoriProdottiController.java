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
