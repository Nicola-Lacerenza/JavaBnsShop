package controllers;

import models.ImmaginiProdotti;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiProdottiController implements Controllers<ImmaginiProdotti> {
    public ImmaginiProdottiController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"immagini_has_prodotti",request);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = -1;
        }
        return output;
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        if(id <= 0 || request == null){
            return false;
        }
        boolean output;
        try(Connection connection = Database.createConnection()){
            output = Database.updateElement(connection, "immagini_has_prodotti", id, request);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        boolean output;
        try(Connection connection = Database.createConnection()){
            output = Database.deleteElement(connection,"immagini_has_prodotti",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<ImmaginiProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<ImmaginiProdotti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"immagini_has_prodotti",objectid,new ImmaginiProdotti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<ImmaginiProdotti> getAllObjects() {
        List<ImmaginiProdotti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"immagini_has_prodotti",new ImmaginiProdotti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<ImmaginiProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
