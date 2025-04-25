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
        Connection connection;
        try{
            connection = Database.createConnection();
            return Database.insertElement(connection,"fornitori",request);
        }catch(SQLException exception){
            exception.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        Connection connection;
        try {
            connection = Database.createConnection();
        }catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }
        return Database.updateElement(connection, "fornitori", id, request);
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        Connection connection;
        try {
            connection = Database.createConnection();
        }catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }
        return Database.deleteElement(connection,"fornitori",objectid);
    }

    @Override
    public Optional<Fornitori> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Connection connection;
        try {
            connection = Database.createConnection();
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        return Database.getElement(connection,"fornitori",objectid,new Fornitori());
    }

    @Override
    public List<Fornitori> getAllObjects() {
        Connection connection;
        try {
            connection = Database.createConnection();
        }catch(SQLException exception){
            exception.printStackTrace();
            return new LinkedList<>();
        }
        return Database.getAllElements(connection,"fornitori",new Fornitori());
    }

    @Override
    public List<Fornitori> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
