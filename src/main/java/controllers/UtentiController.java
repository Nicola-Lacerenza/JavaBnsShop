package controllers;

import bnsshop.bnsshop.RegisterServlet;
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

    public boolean checkEmail(String email){
        String query = "SELECT * FROM utenti WHERE email='"+email+"'";
        List<Utenti> user = Database.executeGenericQuery("utenti",new Utenti(),query);
        return !user.isEmpty();
    }

    public boolean checkAdmin(String email){
        String query = "SELECT * FROM utenti WHERE email='"+email+"'";
        List<Utenti> user = Database.executeGenericQuery("utenti",new Utenti(),query);
        if (user.isEmpty()){
            return false;
        }
        return user.getFirst().getRuolo().equals("admin");
    }

    public Optional<Utenti> getUserByEmail(String email){
        String query = "SELECT * FROM utenti WHERE email='"+email+"'";
        List<Utenti> user = Database.executeGenericQuery("utenti",new Utenti(),query);
        if (user.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(user.getFirst());
    }


    public List<Utenti> executeQuery(String query){
        return Database.executeGenericQuery("utenti",new Utenti(),query);
    }
}

