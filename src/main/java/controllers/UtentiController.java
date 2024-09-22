package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Utenti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UtentiController implements Controllers<Utenti> {

    public UtentiController() {
    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"utenti");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "utenti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"utenti");
    }

    @Override
    public Optional<Utenti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"utenti",new Utenti());
    }

    @Override
    public List<Utenti> getAllObjects() {
        return Database.getAllElements("utenti",new Utenti());
    }
}

