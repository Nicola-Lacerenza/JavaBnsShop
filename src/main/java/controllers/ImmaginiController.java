package controllers;

import models.Immagini;


import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiController implements Controllers<Immagini> {

    public ImmaginiController(){

    }

    @Override
    public Optional<Immagini> insertObject(Map<String, String> request) {
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
    public Optional<Immagini> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Immagini> getAllObjects() {
        return null;
    }
}
