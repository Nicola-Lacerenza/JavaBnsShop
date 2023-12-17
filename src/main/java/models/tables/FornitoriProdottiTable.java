package models.tables;

import models.FornitoriProdotti;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriProdottiTable implements Tables <FornitoriProdotti> {

    @Override
    public boolean insertElement(FornitoriProdotti oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idfornitore","" + oggetto.getIdFornitore());
        fields.put("idprodotti","" + oggetto.getIdProdotti());
        fields.put("data","" + oggetto.getData());
        fields.put("importo","" + oggetto.getImporto());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.insertElement(fields,"FORNITORIPRODOTTI");
    }

    @Override
    public boolean updateElement(FornitoriProdotti oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idfornitore","" + oggetto.getIdFornitore());
        fields.put("idprodotti","" + oggetto.getIdProdotti());
        fields.put("data","" + oggetto.getData());
        fields.put("importo","" + oggetto.getImporto());
        fields.put("descrizione","" + oggetto.getDescrizione());
        return Database.updateElement(fields,"FORNITORIPRODOTTI");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"FORNITORIPRODOTTI");
    }

    @Override
    public Optional<FornitoriProdotti> getElement(int id) {
        return Database.getElement(id,"FORNITORIPRODOTTI");
    }

    @Override
    public List<FornitoriProdotti> getAllElements() {
        return Database.getAllElements("FORNITORIPRODOTTI");
    }
}
