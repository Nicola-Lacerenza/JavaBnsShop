package controllers;

import models.CodiceSconto;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CodiceScontoController implements Controllers<CodiceSconto> {
    public CodiceScontoController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        //dati necessari per l'inserimento nella tabella codice_sconto.
        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        for(int i = 0;i <= 9;i++){
            fields1.put(i,request.get(i).copy());
        }

        //transazione sul database.
        AtomicInteger idCodiceSconto = new AtomicInteger();
        boolean success = Database.executeTransaction(connection -> {
            idCodiceSconto.set(Database.insertElement(connection,"codice_sconto",fields1));
            if(idCodiceSconto.get() <= 0){
                return false;
            }
            Map<Integer,QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
            try{
                fields2.put(0,new QueryFields<>("id_categoria",request.get(10).getFieldValue(),TipoVariabile.longNumber));
                fields2.put(1,new QueryFields<>("id_codicesconto",idCodiceSconto.get(),TipoVariabile.longNumber));
            }catch(SQLException exception){
                exception.printStackTrace();
                return false;
            }
            int idCodiceScontoCategoria = Database.insertElement(connection,"codice_sconto_has_categoria",fields2);
            return idCodiceScontoCategoria > 0;
        });
        if(success){
            return idCodiceSconto.get();
        }
        return -1;
    }

    @Override
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        if(id <= 0 || request == null){
            return false;
        }
        boolean output;
        try(Connection connection = Database.createConnection()){
            output = Database.updateElement(connection, "codice_sconto", id, request);
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
        return Database.executeTransaction(connection -> {
            String query1 = "DELETE FROM codice_sconto_has_categoria WHERE id_codicesconto = ?";
            Map<Integer,QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
            try{
                fields.put(0,new QueryFields<>("id_codicesconto",objectid,TipoVariabile.longNumber));
            }catch(SQLException exception){
                exception.printStackTrace();
                return false;
            }
            boolean deleteResult = Database.executeGenericUpdate(connection,query1,fields);
            if(!deleteResult){
                return false;
            }
            return Database.deleteElement(connection,"codice_sconto",objectid);
        });
    }


    @Override
    public Optional<CodiceSconto> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<CodiceSconto> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"codice_sconto",objectid,new CodiceSconto());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<CodiceSconto> getAllObjects() {
        List<CodiceSconto> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"codice_sconto",new CodiceSconto());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<CodiceSconto> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
