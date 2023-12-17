package models.tables;

import models.Fornitori;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriTable implements Tables <Fornitori> {

    @Override
    public boolean insertElement(Fornitori oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idfornitore","" + oggetto.getIdFornitore());
        fields.put("nome","" + oggetto.getNome());
        fields.put("cognome","" + oggetto.getCognome());
        return Database.insertElement(fields,"FORNITORI");
    }

    @Override
    public boolean updateElement(Fornitori oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idfornitore","" + oggetto.getIdFornitore());
        fields.put("nome","" + oggetto.getNome());
        fields.put("cognome","" + oggetto.getCognome());
        return Database.updateElement(fields,"FORNITORI");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"FORNITORI");
    }

    @Override
    public Optional<Fornitori> getElement(int id) {
        return Database.getElement(id,"FORNITORI");
    }

    @Override
    public List<Fornitori> getAllElements() {
        return Database.getAllElements("FORNITORI");
    }
}
