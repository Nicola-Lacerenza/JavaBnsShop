package controllers;

import models.Modello;
import models.tables.ModelloTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloController implements Controllers<Modello> {
    private ModelloTable tabella;
    public ModelloController(){
        this.tabella= new ModelloTable();
    }

    @Override
    public Optional<Modello> insertObject(Map<String, String> request) {
        int idModello = Integer.parseInt(request.get("idmodello"));
        int idBrand = Integer.parseInt(request.get("idbrand"));
        int idCategoria = Integer.parseInt(request.get("idcategoria"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        Modello modello = new Modello(0,0,0,nome,descrizione);
        if (this.tabella.insertElement(modello)){
            return Optional.of(modello);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idModello = Integer.parseInt(request.get("idmodello"));
        int idBrand = Integer.parseInt(request.get("idbrand"));
        int idCategoria = Integer.parseInt(request.get("idcategoria"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        Modello modello = new Modello(idBrand,idModello,idCategoria,nome,descrizione);
        return this.tabella.updateElement(modello,idModello);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Modello> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Modello> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
