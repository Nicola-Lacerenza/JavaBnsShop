package controllers;

import models.Categoria;
import models.tables.CategoriaTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaController implements Controllers<Categoria> {
    private CategoriaTable tabella;
    public CategoriaController(){
        this.tabella= new CategoriaTable();
    }

    @Override
    public Optional<Categoria> insertObject(Map<String, String> request) {
        int idCategoria = Integer.parseInt(request.get("idcategoria"));
        String nomecategoria = String.valueOf(Integer.parseInt(request.get("nomecategoria")));
        Categoria categoria = new Categoria(0,nomecategoria);
        if (this.tabella.insertElement(categoria)){
            return Optional.of(categoria);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idCategoria = Integer.parseInt(request.get("idcategoria"));
        String nomecategoria = String.valueOf(Integer.parseInt(request.get("nomecategoria")));
        Categoria categoria = new Categoria(idCategoria,nomecategoria);
        return this.tabella.updateElement(categoria,idCategoria);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Categoria> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Categoria> getAllObjects() {
        return this.tabella.getAllElements();
    }
}
