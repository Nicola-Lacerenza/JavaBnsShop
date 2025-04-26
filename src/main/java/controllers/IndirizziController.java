package controllers;

import models.Indirizzi;
import models.Utenti;
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

public class IndirizziController  implements Controllers<Indirizzi>{
    private final String email;

    public IndirizziController(String email) {
        this.email = email;
    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        Integer userId = (Integer)(request.get(0).getFieldValue());
        if (userId <= 0){
            return -1;
        }
        //dati da inserire nella tabella indirizzi.
        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        int j = 1;
        for(int i = 0;i <= 7;i++){
            fields1.put(i,request.get(j).copy());
            j++;
        }

        //transazione sul database.
        AtomicInteger idIndirizzo = new AtomicInteger();
        boolean success = Database.executeTransaction(connection -> {
            idIndirizzo.set(Database.insertElement(connection,"indirizzi",fields1));
            if(idIndirizzo.get() <= 0){
                return false;
            }
            Map<Integer,QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
            try{
                fields2.put(0,new QueryFields<>("id_utente",userId,TipoVariabile.longNumber));
                fields2.put(1,new QueryFields<>("id_indirizzo",idIndirizzo.get(),TipoVariabile.longNumber));
            }catch(SQLException exception){
                exception.printStackTrace();
                return false;
            }
            int idIndirizzoUtente = Database.insertElement(connection,"indirizzi_has_utenti",fields2);
            return idIndirizzoUtente > 0;
        });
        if(success){
            return idIndirizzo.get();
        }
        return -1;
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        if(id <= 0 || request == null){
            return false;
        }
        boolean output;
        try(Connection connection = Database.createConnection()){
            output = Database.updateElement(connection, "indirizzi", id, request);
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
            output = Database.deleteElement(connection,"indirizzi",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<Indirizzi> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<Indirizzi> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"indirizzi",objectid,new Indirizzi());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Indirizzi> getAllObjects() {
        UtentiController controllerUtenti = new UtentiController();
        Optional<Utenti> utente = controllerUtenti.getUserByEmail(email);
        if(utente.isEmpty()){
            return new LinkedList<>();
        }
        int idUtente = utente.get().getId();
        List<Indirizzi> indirizzi;
        try(Connection connection = Database.createConnection()){
            String query = "SELECT * FROM indirizzi_has_utenti JOIN indirizzi ON indirizzi.id = indirizzi_has_utenti.id_indirizzo WHERE id_utente = ?";
            Map<Integer,QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
            fields.put(0,new QueryFields<>("id_utente",idUtente,TipoVariabile.longNumber));
            indirizzi = Database.executeGenericQuery(connection,query,fields,new Indirizzi());
        }catch(SQLException exception){
            exception.printStackTrace();
            indirizzi = new LinkedList<>();
        }
        return indirizzi;

    }

    @Override
    public List<Indirizzi> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
