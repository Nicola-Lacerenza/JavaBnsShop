package controllers;

import models.Fornitori;
import models.tables.FornitoriTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriController implements Controllers<Fornitori> {
    private FornitoriTable tabella;
    public FornitoriController(){
        this.tabella= new FornitoriTable();
    }

    @Override
    public Optional<Fornitori> insertObject(Map<String, String> request) {
        int idFornitore = Integer.parseInt(request.get("idfornitore"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String cognome = String.valueOf(Integer.parseInt(request.get("cognome")));
        Fornitori fornitore = new Fornitori(0,nome,cognome);
        if (this.tabella.insertElement(fornitore)){
            return Optional.of(fornitore);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idFornitore = Integer.parseInt(request.get("idfornitore"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String cognome = String.valueOf(Integer.parseInt(request.get("cognome")));
        Fornitori brand = new Fornitori(idFornitore,nome,cognome);
        return this.tabella.updateElement(brand,idFornitore);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Fornitori> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Fornitori> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
