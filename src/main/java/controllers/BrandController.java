package controllers;

import models.Brand;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrandController implements Controllers<Brand> {

    public BrandController(){
    }

    @Override
    public Optional<Brand> insertObject(Map<String, String> request) {
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
    public Optional<Brand> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Brand> getAllObjects() {
        return new LinkedList<>();
    }
}
