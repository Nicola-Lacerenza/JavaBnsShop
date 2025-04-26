package controllers;

import models.FornitoriProdotti;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FornitoriProdottiController implements Controllers<FornitoriProdotti> {
    public FornitoriProdottiController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"fornitori_has_prodotti",request);
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
            output = Database.updateElement(connection, "fornitori_has_prodotti", id, request);
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
            output = Database.deleteElement(connection,"fornitori_has_prodotti",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<FornitoriProdotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<FornitoriProdotti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"fornitori_has_prodotti",objectid,new FornitoriProdotti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<FornitoriProdotti> getAllObjects() {
        List<FornitoriProdotti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"fornitori_has_prodotti",new FornitoriProdotti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<FornitoriProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
