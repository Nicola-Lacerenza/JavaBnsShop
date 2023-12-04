package models.tables;

import models.Prodotti;
import utility.Database;

import java.util.HashMap;
import java.util.Map;

public class ProdottiTable implements Tables <Prodotti> {

    @Override
    public boolean insertElement(Prodotti oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("id_modello","" + oggetto.getIdModello());
        fields.put("id_taglia","" + oggetto.getIdTaglia());
        fields.put("prezzo","" + oggetto.getPrezzo());
        fields.put("quantita","" + oggetto.getQuantita());
        fields.put("statopubblicazione","" + oggetto.getStatoPubblicazione());
        return Database.insertElement(fields,"PRODOTTI");
    }

    @Override
    public boolean updateElement(Prodotti oggetto, String id) {
        return false;
    }

    @Override
    public boolean deleteElement(String id) {
        return false;
    }

    @Override
    public boolean getElement(String id) {
        return false;
    }

    @Override
    public boolean getAllElements() {
        return false;
    }
}
