package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.TaglieProdotti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaglieProdottiController implements Controllers<TaglieProdotti> {

    public TaglieProdottiController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"taglie_has_prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "taglie_has_prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"taglie_has_prodotti");
    }

    @Override
    public Optional<TaglieProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"taglie_has_prodotti",new TaglieProdotti());
    }

    @Override
    public List<TaglieProdotti> getAllObjects() {
        return Database.getAllElements("taglie_has_prodotti",new TaglieProdotti());
    }

    @Override
    public List<TaglieProdotti> executeQuery(String query) {
        return null;
    }
}
