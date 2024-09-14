package controllers;

import models.Utenti;
import utility.Database;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UtentiController implements Controllers<Utenti> {

    public UtentiController() {
    }

    @Override
    public boolean insertObject(Map<String, String> request) {
        return Database.insertElement(request,"utenti");
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int id = Integer.parseInt(request.get("id"));
        return Database.updateElement(id,request, "utenti");
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

