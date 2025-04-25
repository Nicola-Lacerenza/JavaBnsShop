package controllers;

import models.ImmaginiProdotti;
import utility.Database;
import utility.QueryFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiProdottiController implements Controllers<ImmaginiProdotti> {

    public ImmaginiProdottiController(){

    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return Database.insertElement(request,"immagini_has_prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
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
    public List<ImmaginiProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
