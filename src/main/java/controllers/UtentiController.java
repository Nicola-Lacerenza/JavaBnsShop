package controllers;

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

public class UtentiController implements Controllers<Utenti> {
    public UtentiController() {}

    @Override
    public int insertObject(Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        if(request == null){
            return -1;
        }
        int output;
        try(Connection connection = Database.createConnection()){
            output = Database.insertElement(connection,"utenti",request);
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
            output = Database.updateElement(connection, "utenti", id, request);
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
            output = Database.deleteElement(connection,"utenti",objectid);
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    @Override
    public Optional<Utenti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        Optional<Utenti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getElement(connection,"utenti",objectid,new Utenti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Utenti> getAllObjects() {
        List<Utenti> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"utenti",new Utenti());
        }catch(SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    public boolean checkEmail(String email){
        Optional<Utenti> utente = getUserByEmail(email);
        return utente.isPresent();
    }

    public boolean checkAdmin(String email){
        Optional<Utenti> utente = getUserByEmail(email);
        if(utente.isPresent()){
            return utente.get().getRuolo().equals("admin");
        }
        return false;
    }

    public Optional<Utenti> getUserByEmail(String email){
        Optional<Utenti> output;
        try(Connection connection = Database.createConnection()){
            String query = "SELECT * FROM utenti WHERE email = ?";
            Map<Integer,QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
            fields.put(0,new QueryFields<>("email",email,TipoVariabile.string));
            List<Utenti> utenti = Database.executeGenericQuery(connection,query,fields,new Utenti());
            if(utenti.isEmpty()){
                output = Optional.empty();
            }else{
                output = Optional.of(utenti.getFirst());
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output = Optional.empty();
        }
        return output;
    }

    @Override
    public List<Utenti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}

