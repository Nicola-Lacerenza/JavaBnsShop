package controllers;

import models.Brand;
import models.tables.BrandTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrandController implements Controllers<Brand>{
    private BrandTable tabella;
    public BrandController(){
        this.tabella= new BrandTable();
    }

    @Override
    public Optional<Brand> insertObject(Map<String, String> request) {
        int idBrand = Integer.parseInt(request.get("idbrand"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        Brand brand = new Brand(0,nome,descrizione);
        if (this.tabella.insertElement(brand)){
            return Optional.of(brand);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idBrand = Integer.parseInt(request.get("idbrand"));
        String nome = String.valueOf(Integer.parseInt(request.get("nome")));
        String descrizione = String.valueOf(Integer.parseInt(request.get("descrizione")));
        Brand brand = new Brand(idBrand,nome,descrizione);
        return this.tabella.updateElement(brand,idBrand);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Brand> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Brand> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
