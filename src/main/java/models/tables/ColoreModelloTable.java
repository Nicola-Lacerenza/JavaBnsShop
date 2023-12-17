package models.tables;

import models.ColoreModello;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreModelloTable implements Tables <ColoreModello> {

    @Override
    public boolean insertElement(ColoreModello oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcolore","" + oggetto.getIdColore());
        fields.put("idmodello","" + oggetto.getIdModello());
        return Database.insertElement(fields,"COLOREMODELLO");
    }

    @Override
    public boolean updateElement(ColoreModello oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcolore","" + oggetto.getIdColore());
        fields.put("idmodello","" + oggetto.getIdModello());
        return Database.updateElement(fields,"COLOREMODELLO");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"COLOREMODELLO");
    }

    @Override
    public Optional<ColoreModello> getElement(int id) {
        return Database.getElement(id,"COLOREMODELLO");
    }

    @Override
    public List<ColoreModello> getAllElements() {
        return Database.getAllElements("COLOREMODELLO");
    }
}
