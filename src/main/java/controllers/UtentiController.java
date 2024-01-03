package controllers;

import com.sun.net.httpserver.Request;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import utility.Database;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UtentiController implements Controllers<String> {

    public UtentiController() {
    }

    @Override
    public Optional<String> insertObject(Map<String, String> request) { return Optional.empty();}

    public Optional<String> insertObject(JSONObject request) {
        if (Database.insertElement(request,"utenti")){
            return Optional.of("Utente registrato correttamente");
        }
        return Optional.empty();
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
    public Optional<String> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<String> getAllObjects() {
        return null;
    }
}

