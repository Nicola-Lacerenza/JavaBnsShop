package controllers;

import models.ResiProdotti;
import utility.QueryFields;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResiProdottiController implements Controllers<ResiProdotti>{
    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return 0;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<ResiProdotti> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<ResiProdotti> getAllObjects() {
        return null;
    }

    @Override
    public List<ResiProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}
