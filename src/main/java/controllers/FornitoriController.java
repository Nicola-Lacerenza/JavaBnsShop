package controllers;

import models.Fornitori;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriController implements Controllers<Fornitori> {
    public FornitoriController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"fornitori",request);
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
            output = Database.updateElement(connection, "fornitori", id, request);
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
            output = Database.deleteElement(connection,"fornitori",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<Fornitori> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<Fornitori> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"fornitori",objectid,new Fornitori());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Fornitori> getAllObjects() {
        List<Fornitori> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"fornitori",new Fornitori());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<Fornitori> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
