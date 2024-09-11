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
        // Estrai l'ID dalla richiesta
        int id = Integer.parseInt(request.get("id"));

        // Chiama il metodo della classe Database per aggiornare l'elemento
        boolean isUpdated = Database.updateElement(id,request, "utenti");

        // Restituisci il risultato dell'operazione di aggiornamento
        return isUpdated;
    }

    @Override
    public boolean deleteObject(int objectid) {
        // Controlla se l'ID è valido
        if (objectid <= 0) {
            return false;
        }

        // Chiama il metodo della classe Database per eliminare l'elemento
        boolean isDeleted = Database.deleteElement(objectid,"utenti");

        // Restituisci il risultato dell'operazione di eliminazione
        return isDeleted;
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

