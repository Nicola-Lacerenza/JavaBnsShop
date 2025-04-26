package controllers;

import models.Colore;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColoreController implements Controllers<Colore> {
    public ColoreController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"colore",request);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = -1;
        }
        return output;
    }

    @Override
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(id <= 0 || request == null){
            return false;
        }
        boolean output;
        try(Connection connection = Database.createConnection()){
            output = Database.updateElement(connection, "colore", id, request);
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
            output = Database.deleteElement(connection,"colore",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<Colore> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<Colore> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"colore",objectid,new Colore());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Colore> getAllObjects() {
        List<Colore> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"colore",new Colore());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<Colore> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
