package controllers;

import models.Indirizzi;
import models.Utenti;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IndirizziController  implements Controllers<Indirizzi>{

    private final String email;

    public IndirizziController(String email) {
        this.email = email;
    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        String userId = request.get(0).getValue();
        if (userId.isEmpty()){
            return Database.insertElement(request,"indirizzi");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        Connection connection = null;
        List<PreparedStatement> statementList = new LinkedList<>();

        boolean output = false;
        try {

            // APERTURA CONNESSIONE
            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false); // Avvia transazione

            String query1 = "INSERT INTO indirizzi(nome,cognome,citta,stato,cap,indirizzo,email,numero_telefono) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, request.get(1).getValue());
            preparedStatement1.setString(2, request.get(2).getValue());
            preparedStatement1.setString(3, request.get(3).getValue());
            preparedStatement1.setString(4, request.get(4).getValue());
            preparedStatement1.setString(5, request.get(5).getValue());
            preparedStatement1.setString(6, request.get(6).getValue());
            preparedStatement1.setString(7, request.get(7).getValue());
            preparedStatement1.setString(8, request.get(8).getValue());
            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            int idIndirizzo = -1;
            if (generatedKeys.next()) {
                idIndirizzo = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Errore: Nessun ID prodotto generato.");
            }

            String query2 = "INSERT INTO indirizzi_has_utenti(id_utente,id_indirizzo) VALUES (?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, Integer.parseInt(userId));
            preparedStatement2.setInt(2, idIndirizzo);
            preparedStatement2.executeUpdate();

            connection.commit(); // Commit delle modifiche solo se tutte le query hanno successo
            output = true;
        } catch (SQLException e) {
            // Gestione errori e rollback in caso di fallimento
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            output = false;
        } finally {
            // Chiusura connessione e statement
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
        return output;
    }

    @Override
    public boolean updateObject(int id,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
        return Database.updateElement(id,request, "indirizzi");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"indirizzi");
    }

    @Override
    public Optional<Indirizzi> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"indirizzi",new Indirizzi());
    }

    @Override
    public List<Indirizzi> getAllObjects() {

        Controllers<Utenti> controllerUtenti = new UtentiController();
        List<Utenti> utenti = controllerUtenti.executeQuery("SELECT * FROM utenti WHERE email= '" + email + "'");

        if(utenti.isEmpty()){
            return new LinkedList<>();
        }

        Utenti utente = utenti.getFirst();
        int idUtente = utente.getId();

        String query = "SELECT * FROM indirizzi_has_utenti JOIN indirizzi ON indirizzi.id = indirizzi_has_utenti.id_indirizzo WHERE id_utente='"+idUtente+"' ";
        List<Indirizzi> indirizzi = Database.executeGenericQuery("indirizzi_has_utenti",new Indirizzi(),query);
        return indirizzi;

    }

    @Override
    public List<Indirizzi> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
