package models.tables;

import models.Brand;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrandTable implements Tables <Brand> {

    @Override
    public boolean insertElement(Brand oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idbrand","" + oggetto.getIdBrand());
        fields.put("nome","" + oggetto.getNome());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.insertElement(fields,"BRAND");
    }

    @Override
    public boolean updateElement(Brand oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idbrand","" + oggetto.getIdBrand());
        fields.put("nome","" + oggetto.getNome());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.updateElement(fields,"BRAND");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"BRAND");
    }

    @Override
    public Optional<Brand> getElement(int id) {
        return Database.getElement(id,"BRAND");
    }

    @Override
    public List<Brand> getAllElements() {
        return Database.getAllElements("BRAND");
    }
}
