package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Immagini;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiController implements Controllers<Immagini> {

    public ImmaginiController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"immagini");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "immagini");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"immagini");
    }

    @Override
    public Optional<Immagini> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"immagini",new Immagini());
    }

    @Override
    public List<Immagini> getAllObjects() {
        return Database.getAllElements("immagini",new Immagini());
    }
}
