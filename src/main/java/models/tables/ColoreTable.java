package models.tables;

import models.Colore;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreTable implements Tables <Colore> {

    @Override
    public boolean insertElement(Colore oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcolore","" + oggetto.getIdColore());
        fields.put("nome","" + oggetto.getNome());
        fields.put("rgb","" + oggetto.getRgb());
        fields.put("hex","" + oggetto.getHex());
        return Database.insertElement(fields,"COLORE");
    }

    @Override
    public boolean updateElement(Colore oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcolore","" + oggetto.getIdColore());
        fields.put("nome","" + oggetto.getNome());
        fields.put("rgb","" + oggetto.getRgb());
        fields.put("hex","" + oggetto.getHex());
        return Database.updateElement(fields,"COLORE");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"COLORE");
    }

    @Override
    public Optional<Colore> getElement(int id) {
        return Database.getElement(id,"COLORE");
    }

    @Override
    public List<Colore> getAllElements() {
        return Database.getAllElements("COLORE");
    }
}
