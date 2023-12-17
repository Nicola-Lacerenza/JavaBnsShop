package controllers;

import models.ColoreModello;
import models.tables.ColoreModelloTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreModelloController implements Controllers<ColoreModello> {
    private ColoreModelloTable tabella;
    public ColoreModelloController(){
        this.tabella= new ColoreModelloTable();
    }

    @Override
    public Optional<ColoreModello> insertObject(Map<String, String> request) {
        int idColore = Integer.parseInt(request.get("idcolore"));
        int idModello = Integer.parseInt(request.get("idmodello"));
        ColoreModello coloremodello = new ColoreModello(0,0);
        if (this.tabella.insertElement(coloremodello)){
            return Optional.of(coloremodello);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idColore = Integer.parseInt(request.get("idcolore"));
        int idModello = Integer.parseInt(request.get("idmodello"));
        ColoreModello coloremodello = new ColoreModello(idColore,idModello);
        return this.tabella.updateElement(coloremodello,idColore);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<ColoreModello> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<ColoreModello> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
