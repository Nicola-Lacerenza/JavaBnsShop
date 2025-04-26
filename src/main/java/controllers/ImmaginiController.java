package controllers;

import models.Immagini;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiController implements Controllers<Immagini> {
    public ImmaginiController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"immagini",request);
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
            output = Database.updateElement(connection, "immagini", id, request);
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
            output = Database.deleteElement(connection,"immagini",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<Immagini> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<Immagini> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"immagini",objectid,new Immagini());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Immagini> getAllObjects() {
        List<Immagini> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"immagini",new Immagini());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<Immagini> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
