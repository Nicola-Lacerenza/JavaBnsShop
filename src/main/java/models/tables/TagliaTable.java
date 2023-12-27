package models.tables;

import models.Taglia;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TagliaTable implements Tables <Taglia> {

    @Override
    public boolean insertElement(Taglia oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idtaglia","" + oggetto.getIdTaglia());
        fields.put("taglia","" + oggetto.getTaglia());
        return Database.insertElement(fields,"TAGLIA");
    }

    @Override
    public boolean updateElement(Taglia oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idtaglia","" + oggetto.getIdTaglia());
        fields.put("taglia","" + oggetto.getTaglia());
        return Database.updateElement(fields,"TAGLIA");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"TAGLIA");
    }

    @Override
    public Optional<Taglia> getElement(int id) {
        return Database.getElement(id,"TAGLIA");
    }

    @Override
    public List<Taglia> getAllElements() {
        return Database.getAllElements("TAGLIA");
    }
}
