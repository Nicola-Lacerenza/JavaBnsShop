package controllers;

import models.Colore;
import models.tables.ColoreTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreController implements Controllers<Colore> {
    private ColoreTable tabella;
    public ColoreController(){
        this.tabella= new ColoreTable();
    }

    @Override
    public Optional<Colore> insertObject(Map<String, String> request) {
        int idColore = Integer.parseInt(request.get("idcolore"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String rgb = String.valueOf(Integer.parseInt(request.get("rgb")));
        String hex = String.valueOf(Integer.parseInt(request.get("hex")));
        Colore colore = new Colore(0,nome,rgb,hex);
        if (this.tabella.insertElement(colore)){
            return Optional.of(colore);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idColore = Integer.parseInt(request.get("idcolore"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String rgb = String.valueOf(Integer.parseInt(request.get("rgb")));
        String hex = String.valueOf(Integer.parseInt(request.get("hex")));
        Colore colore = new Colore(idColore,nome,rgb,hex);
        return this.tabella.updateElement(colore,idColore);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Colore> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Colore> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
