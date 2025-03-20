package controllers;

import bnsshop.bnsshop.IndirizziServlet;
import bnsshop.bnsshop.RegisterServlet;
import models.FornitoriProdotti;
import models.Indirizzi;
import models.Modello;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IndirizziController  implements Controllers<Indirizzi>{
    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"indirizzi");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "indirizzi");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"indirizzi");
    }

    @Override
    public Optional<Indirizzi> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"indirizzi",new Indirizzi());
    }

    @Override
    public List<Indirizzi> getAllObjects() {
        return Database.getAllElements("indirizzi",new Indirizzi());
    }
}
