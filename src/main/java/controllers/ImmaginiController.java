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
    public boolean updateObject(Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        return Database.updateElement(id,request, "immagini");
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Immagini> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Immagini> getAllObjects() {
        return null;
    }
}
