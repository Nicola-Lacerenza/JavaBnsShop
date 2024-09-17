package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Categoria;
import utility.Database;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrandController implements Controllers<Brand> {

    public BrandController(){
    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"brand");
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        return Database.updateElement(id,request, "brand");
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Brand> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Brand> getAllObjects() {return new LinkedList<>(); }
}
