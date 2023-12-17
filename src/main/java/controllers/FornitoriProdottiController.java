package controllers;

import models.FornitoriProdotti;
import models.tables.FornitoriProdottiTable;

import java.util.*;

public class FornitoriProdottiController implements Controllers<FornitoriProdotti> {
    private FornitoriProdottiTable tabella;
    public FornitoriProdottiController(){
        this.tabella= new FornitoriProdottiTable();
    }

    @Override
    public Optional<FornitoriProdotti> insertObject(Map<String, String> request) {
        int idFornitore = Integer.parseInt(request.get("idfornitore"));
        int idProdotti = Integer.parseInt(request.get("idprodotti"));
        Calendar data = Calendar.getInstance(TimeZone.getTimeZone(request.get("data")));
        int importo = Integer.parseInt(request.get("importo"));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        FornitoriProdotti fornitoriProdotti = new FornitoriProdotti(0,0,data,importo,descrizione);
        if (this.tabella.insertElement(fornitoriProdotti)){
            return Optional.of(fornitoriProdotti);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idFornitore = Integer.parseInt(request.get("idfornitore"));
        int idProdotti = Integer.parseInt(request.get("idprodotti"));
        Calendar data = Calendar.getInstance(TimeZone.getTimeZone(request.get("data")));
        int importo = Integer.parseInt(request.get("importo"));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        FornitoriProdotti fornitoriProdotti = new FornitoriProdotti(idFornitore,idProdotti,data,importo,descrizione);
        return this.tabella.updateElement(fornitoriProdotti,idFornitore);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<FornitoriProdotti> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<FornitoriProdotti> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
