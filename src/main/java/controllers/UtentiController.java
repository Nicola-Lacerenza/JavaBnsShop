package controllers;

import com.sun.net.httpserver.Request;
import models.Utenti;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UtentiController implements Controllers<Utenti> {

    public UtentiController() {
    }

    @Override
    public Optional<Utenti> insertObject(Map<String, String> request) {
        Utenti u = Database.insertElement(request,"utenti",new Utenti());
        if (u==null){
            return Optional.empty();
        }
        return Optional.of(u);
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Utenti> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Utenti> getAllObjects() {
        return null;
    }
}

