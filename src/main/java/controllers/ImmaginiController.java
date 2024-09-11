package controllers;

import models.Immagini;
import models.Modello;
import utility.Database;


import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiController implements Controllers<Immagini> {

    public ImmaginiController(){

    }

    @Override
    public Optional<Immagini> insertObject(Map<String, String> request) {
        Immagini i = Database.insertElement(request,"immagini",new Immagini());
        if (i==null){
            return Optional.empty();
        }
        return Optional.of(i);
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        // Estrai l'ID dalla richiesta
        int id = Integer.parseInt(request.get("idImmagini"));

        // Chiama il metodo della classe Database per aggiornare l'elemento
        boolean isUpdated = Database.updateElement(id,request, "immagini");

        // Restituisci il risultato dell'operazione di aggiornamento
        return isUpdated;
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
