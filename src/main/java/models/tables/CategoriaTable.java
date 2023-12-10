package models.tables;

import models.Categoria;
import utility.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoriaTable implements Tables <Categoria> {

    @Override
    public boolean insertElement(Categoria oggetto) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcategoria","" + oggetto.getIdCategoria());
        fields.put("nomecategoria","" + oggetto.getNomeCategoria());
        return Database.insertElement(fields,"CATEGORIA");
    }

    @Override
    public boolean updateElement(Categoria oggetto, int id) {
        Map<String,String> fields= new HashMap<>();
        fields.put("idcategoria","" + oggetto.getIdCategoria());
        fields.put("nomecategoria","" + oggetto.getNomeCategoria());
        return Database.updateElement(fields,"CATEGORIA");
    }

    @Override
    public boolean deleteElement(int id) {
        return Database.deleteElement(id,"CATEGORIA");
    }

    @Override
    public Optional<Categoria> getElement(int id) {
        return Database.getElement(id,"CATEGORIA");
    }

    @Override
    public List<Categoria> getAllElements() {
        return Database.getAllElements("CATEGORIA");
    }
}
