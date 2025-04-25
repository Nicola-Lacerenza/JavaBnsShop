package controllers;

import models.Categoria;
import utility.Database;
import utility.QueryFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaController implements Controllers<Categoria> {
    public CategoriaController(){
    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return Database.insertElement(request,"categoria");
    }

    @Override
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return Database.updateElement(id,request, "categoria");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"categoria");
    }

    @Override
    public Optional<Categoria> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"categoria",new Categoria());
    }

    @Override
    public List<Categoria> getAllObjects() {
        return Database.getAllElements("categoria",new Categoria());

    }

    @Override
    public List<Categoria> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
