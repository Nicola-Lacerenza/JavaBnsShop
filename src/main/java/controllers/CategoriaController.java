package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Categoria;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaController implements Controllers<Categoria> {
    public CategoriaController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"categoria");
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        return Database.updateElement(id,request, "categoria");
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Categoria> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Categoria> getAllObjects() {
        return null;
    }
}
