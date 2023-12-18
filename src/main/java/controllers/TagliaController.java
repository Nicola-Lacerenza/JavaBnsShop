package controllers;

import models.Taglia;
import models.tables.TagliaTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TagliaController implements Controllers<Taglia> {
    private TagliaTable tabella;
    public TagliaController(){
        this.tabella= new TagliaTable();
    }

    @Override
    public Optional<Taglia> insertObject(Map<String, String> request) {
        int idTaglia = Integer.parseInt(request.get("idtaglia"));
        String taglia = String.valueOf(Integer.parseInt(request.get("taglia")));
        Taglia taglia1 = new Taglia(0,taglia);
        if (this.tabella.insertElement(taglia1)){
            return Optional.of(taglia1);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idTaglia = Integer.parseInt(request.get("idtaglia"));
        String taglia = String.valueOf(Integer.parseInt(request.get("taglia")));
        Taglia taglia1 = new Taglia(idTaglia,taglia);
        return this.tabella.updateElement(taglia1,idTaglia);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Taglia> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Taglia> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
