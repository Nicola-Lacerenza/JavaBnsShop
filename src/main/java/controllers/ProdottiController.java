package controllers;

import models.Prodotti;
import models.tables.ProdottiTable;

import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {
    private ProdottiTable tabella;
    public ProdottiController(){
        this.tabella= new ProdottiTable();
    }

    @Override
    public Optional<Prodotti> insertObject(Map<String, String> request) {
        Prodotti prodotto = new Prodotti(0,1,1,1,1,1);
        if (this.tabella.insertElement(prodotto)){
            return Optional.of(prodotto);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        return false;
    }

    @Override
    public boolean deleteObject(Map<String, String> request) {
        return false;
    }

    @Override
    public String getObject(Map<String, String> request) {
        return null;
    }

    @Override
    public String getAllObjects(Map<String, String> request) {
        return null;
    }
}
