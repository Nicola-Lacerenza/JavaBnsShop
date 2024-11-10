package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Prodotti;
import utility.Database;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {

    public ProdottiController(){
    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        List<String> queries = new LinkedList<>();
        queries.add("INSERT INTO immagini (url) VALUES ('"+request.get(0).getValue()+"',)");
        queries.add("INSERT INTO prodotti (id_modello,id_taglia,id_immagini,prezzo,quantita,stato_pubblicazione)" +
                "VALUES ('"+request.get(1).getValue()+"','"+request.get(2).getValue()+"','"+request.get(3).getValue()+"'," +
                "'"+request.get(4).getValue()+"','"+request.get(5).getValue()+"','"+request.get(6).getValue()+"',");
        return Database.executeQueries(queries);
    }

    @Override
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"prodotti");
    }

    @Override
    public Optional<Prodotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"prodotti",new Prodotti());
    }

    @Override
    public List<Prodotti> getAllObjects() {
        return Database.getAllElements("prodotti",new Prodotti());
    }
}
