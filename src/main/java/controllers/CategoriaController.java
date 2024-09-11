package controllers;

import models.Categoria;
import models.Colore;
import utility.Database;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaController implements Controllers<Categoria> {
    public CategoriaController(){

    }

    @Override
    public Optional<Categoria> insertObject(Map<String, String> request) {
        Categoria c = Database.insertElement(request,"categoria",new Categoria());
        if (c==null){
            return Optional.empty();
        }
        return Optional.of(c);
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        // Estrai l'ID dalla richiesta
        int id = Integer.parseInt(request.get("idCategoria"));

        // Chiama il metodo della classe Database per aggiornare l'elemento
        boolean isUpdated = Database.updateElement(id,request, "categoria");

        // Restituisci il risultato dell'operazione di aggiornamento
        return isUpdated;
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
