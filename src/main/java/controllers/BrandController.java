package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import utility.Database;
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
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "brand");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"brand");
    }

    @Override
    public Optional<Brand> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"brand",new Brand());
    }

    @Override
    public List<Brand> getAllObjects() {
        return Database.getAllElements("brand",new Brand());
    }

    @Override
    public List<Brand> executeQuery(String query) {
        return null;
    }
}
