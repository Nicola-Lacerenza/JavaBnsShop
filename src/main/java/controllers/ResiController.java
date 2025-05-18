package controllers;

import models.Resi;
import models.ResiProdotti;
import models.Taglia;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResiController implements Controllers<Resi>{

    public int insertObject(List<Map<Integer, QueryFields<? extends Comparable<?>>>> request){
        return -1;
    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return -1;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Resi> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<Resi> getAllObjects() {
        List<Resi> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"resi",new Resi());
        }catch (SQLException e){
            e.printStackTrace();
            output= new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<Resi> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}
