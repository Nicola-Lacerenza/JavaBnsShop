package controllers;

import bnsshop.bnsshop.RegisterServlet;

import models.ImmaginiProdotti;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiProdottiController implements Controllers<ImmaginiProdotti> {

    public ImmaginiProdottiController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"immagini_has_prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "immagini_has_prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"immagini_has_prodotti");
    }

    @Override
    public Optional<ImmaginiProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"immagini_has_prodotti",new ImmaginiProdotti());
    }

    @Override
    public List<ImmaginiProdotti> getAllObjects() {
        return Database.getAllElements("immagini_has_prodotti",new ImmaginiProdotti());
    }

    @Override
    public List<ImmaginiProdotti> executeQuery(String query) {
        return null;
    }
}
