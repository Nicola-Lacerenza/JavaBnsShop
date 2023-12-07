package controllers;

import models.Prodotti;
import models.tables.ProdottiTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {
    private ProdottiTable tabella;
    public ProdottiController(){
        this.tabella= new ProdottiTable();
    }

    @Override
    public Optional<Prodotti> insertObject(Map<String, String> request) {
        int idProdotto = Integer.parseInt(request.get("idprodotto"));
        int idModello = Integer.parseInt(request.get("idmodello"));
        int idTaglia = Integer.parseInt(request.get("idtaglia"));
        double prezzo = Double.parseDouble(request.get("prezzo"));
        int quantita = Integer.parseInt(request.get("quantita"));
        int statopubblicazione = Integer.parseInt(request.get("statopubblicazione"));
        Prodotti prodotto = new Prodotti(0,idModello,idTaglia,prezzo,quantita,statopubblicazione);
        if (this.tabella.insertElement(prodotto)){
            return Optional.of(prodotto);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idProdotto = Integer.parseInt(request.get("idprodotto"));
        int idModello = Integer.parseInt(request.get("idmodello"));
        int idTaglia = Integer.parseInt(request.get("idtaglia"));
        double prezzo = Double.parseDouble(request.get("prezzo"));
        int quantita = Integer.parseInt(request.get("quantita"));
        int statopubblicazione = Integer.parseInt(request.get("statopubblicazione"));
        Prodotti prodotto = new Prodotti(idProdotto,idModello,idTaglia,prezzo,quantita,statopubblicazione);
        return this.tabella.updateElement(prodotto,idProdotto);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Prodotti> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Prodotti> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
