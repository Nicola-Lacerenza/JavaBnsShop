package controllers;

import models.CodiceSconto;
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

public class CodiceScontoController implements Controllers<CodiceSconto> {

    public CodiceScontoController(){

    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
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

            //region INSERIMENTO PRODOTTO + ESTRAZIONE ID

            String query1 = "INSERT INTO codice_sconto(codice,valore,descrizione,tipo,data_inizio,data_fine,uso_massimo,uso_per_utente,minimo_acquisto,attivo) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, request.get(0).getValue());
            preparedStatement1.setInt(2, Integer.parseInt(request.get(1).getValue()));
            preparedStatement1.setString(3, request.get(2).getValue());
            preparedStatement1.setString(4, request.get(3).getValue());
            preparedStatement1.setString(5, request.get(4).getValue());
            preparedStatement1.setString(6, request.get(5).getValue());
            preparedStatement1.setInt(7, Integer.parseInt(request.get(6).getValue()));
            preparedStatement1.setInt(8, Integer.parseInt(request.get(7).getValue()));
            preparedStatement1.setInt(9, Integer.parseInt(request.get(8).getValue()));
            preparedStatement1.setInt(10, Integer.parseInt(request.get(9).getValue()));
            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            int idCodiceSconto = -1;
            if (generatedKeys.next()) {
                idCodiceSconto = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Errore: Nessun ID Codice Sconto generato.");
            }

            String query2 = "INSERT INTO codice_sconto_has_categoria(id_categoria,id_codicesconto)VALUES (?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, Integer.parseInt(request.get(10).getValue()));
            preparedStatement2.setInt(2, idCodiceSconto);
            preparedStatement2.executeUpdate();

//endregion

            connection.commit(); // Commit delle modifiche solo se tutte le query hanno successo
            output = true;
        } catch (SQLException e) {
            // Gestione errori e rollback in caso di fallimento            e.printStackTrace();
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
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {

        return Database.updateElement(id,request, "codice_sconto");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
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

            // ELIMINA I RIFERIMENTI DALLA TABELLA CODICESCONTO_HAS_CATEGORIA

            String query1 = "DELETE FROM `codice_sconto_has_categoria` WHERE id_codicesconto = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, objectid);
            preparedStatement1.executeUpdate();

            // ELIMINA I RIFERIMENTI DALLA TABELLA CODICESCONTO_HAS_CATEGORIA

            String query2 = "DELETE FROM `codice_sconto` WHERE id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, objectid);
            preparedStatement2.executeUpdate();

            connection.commit();
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
    public Optional<CodiceSconto> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"codice_sconto",new CodiceSconto());
    }

    @Override
    public List<CodiceSconto> getAllObjects() {
        return Database.getAllElements("codice_sconto",new CodiceSconto());
    }

    @Override
    public List<CodiceSconto> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return List.of();
    }
}
