package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.ColoreProdotti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreProdottiController implements Controllers<ColoreProdotti> {

    public ColoreProdottiController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"colore_has_prodotti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "colore_has_prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"colore_has_prodotti");
    }

    @Override
    public Optional<ColoreProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"colore_has_prodotti",new ColoreProdotti());
    }

    @Override
    public List<ColoreProdotti> getAllObjects() {
        return Database.getAllElements("colore_has_prodotti",new ColoreProdotti());
    }
}
