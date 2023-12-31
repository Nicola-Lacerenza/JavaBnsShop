package models.tables;

import models.Prodotti;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiTable implements Tables <Prodotti> {

    @Override
    public boolean insertElement(Prodotti oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idprodotto","" + oggetto.getIdProdotto());
        fields.put("idmodello","" + oggetto.getIdModello());
        fields.put("idtaglia","" + oggetto.getIdTaglia());
        fields.put("prezzo","" + oggetto.getPrezzo());
        fields.put("quantita","" + oggetto.getQuantita());
        fields.put("statopubblicazione","" + oggetto.getStatoPubblicazione());
        return Database.insertElement(fields,"PRODOTTI");
    }

    @Override
    public boolean updateElement(Prodotti oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idprodotto","" + oggetto.getIdProdotto());
        fields.put("idmodello","" + oggetto.getIdModello());
        fields.put("idtaglia","" + oggetto.getIdTaglia());
        fields.put("prezzo","" + oggetto.getPrezzo());
        fields.put("quantita","" + oggetto.getQuantita());
        fields.put("statopubblicazione","" + oggetto.getStatoPubblicazione());
        return Database.updateElement(fields,"PRODOTTI");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"PRODOTTI");
    }

    @Override
    public Optional<Prodotti> getElement(int id) {
        return Database.getElement(id,"PRODOTTI");
    }

    @Override
    public List<Prodotti> getAllElements() {
        return Database.getAllElements("PRODOTTI");
    }
}
