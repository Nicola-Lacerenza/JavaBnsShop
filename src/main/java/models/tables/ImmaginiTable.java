package models.tables;

import models.Immagini;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiTable implements Tables <Immagini> {

    @Override
    public boolean insertElement(Immagini oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idimmagini","" + oggetto.getIdImmagini());
        fields.put("idprodotti","" + oggetto.getIdProdotti());
        fields.put("url","" + oggetto.getUrl());
        return Database.insertElement(fields,"IMMAGINI");
    }

    @Override
    public boolean updateElement(Immagini oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idimmagini","" + oggetto.getIdImmagini());
        fields.put("idprodotti","" + oggetto.getIdProdotti());
        fields.put("url","" + oggetto.getUrl());
        return Database.updateElement(fields,"IMMAGINI");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"IMMAGINI");
    }

    @Override
    public Optional<Immagini> getElement(int id) {
        return Database.getElement(id,"IMMAGINI");
    }

    @Override
    public List<Immagini> getAllElements() {
        return Database.getAllElements("BRAND");
    }
}
