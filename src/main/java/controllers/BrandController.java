package controllers;

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
    public Optional<Brand> insertObject(Map<String, String> request) {
        Brand b = Database.insertElement(request,"brand",new Brand());
        if (b==null){
            return Optional.empty();
        }
        return Optional.of(b);
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        // Estrai l'ID dalla richiesta
        int id = Integer.parseInt(request.get("idBrand"));

        // Chiama il metodo della classe Database per aggiornare l'elemento
        boolean isUpdated = Database.updateElement(id,request, "brand");

        // Restituisci il risultato dell'operazione di aggiornamento
        return isUpdated;
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
