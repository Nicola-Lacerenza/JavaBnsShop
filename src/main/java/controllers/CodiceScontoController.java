package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.CodiceSconto;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CodiceScontoController implements Controllers<CodiceSconto> {

    public CodiceScontoController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"codice_sconto");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "codice_sconto");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"codice_sconto");
    }

    @Override
    public Optional<CodiceSconto> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"codice_sconto",new CodiceSconto());
    }

    @Override
    public List<CodiceSconto> getAllObjects() {
        return Database.getAllElements("codice_sconto",new CodiceSconto());
    }

    @Override
    public List<CodiceSconto> executeQuery(String query) {
        return null;
    }
}
