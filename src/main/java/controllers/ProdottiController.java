package controllers;

import models.Prodotti;

import java.util.Map;

public class ProdottiController implements Controllers<Prodotti> {


    @Override
    public Prodotti insertObject(Map<String, String> request) {
        return null;
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
