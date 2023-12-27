package models.tables;

import models.Modello;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloTable implements Tables <Modello> {

    @Override
    public boolean insertElement(Modello oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idmodello","" + oggetto.getIdModello());
        fields.put("idbrand","" + oggetto.getIdBrand());
        fields.put("idcategoria","" + oggetto.getIdCategoria());
        fields.put("nome","" + oggetto.getNome());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.insertElement(fields,"MODELLO");
    }

    @Override
    public boolean updateElement(Modello oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idmodello","" + oggetto.getIdModello());
        fields.put("idbrand","" + oggetto.getIdBrand());
        fields.put("idcategoria","" + oggetto.getIdCategoria());
        fields.put("nome","" + oggetto.getNome());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.updateElement(fields,"MODELLO");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"MODELLO");
    }

    @Override
    public Optional<Modello> getElement(int id) {
        return Database.getElement(id,"MODELLO");
    }

    @Override
    public List<Modello> getAllElements() {
        return Database.getAllElements("MODELLO");
    }
}
