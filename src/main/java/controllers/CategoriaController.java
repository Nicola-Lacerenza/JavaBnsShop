package controllers;

import models.Categoria;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaController implements Controllers<Categoria> {
    public CategoriaController(){

    }

    @Override
    public Optional<Categoria> insertObject(Map<String, String> request) {
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Categoria> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Categoria> getAllObjects() {
        return null;
    }
}
