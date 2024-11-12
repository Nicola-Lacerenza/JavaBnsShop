package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Brand;
import models.Modello;
import utility.Database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelloController implements Controllers<Modello> {

    public ModelloController(){

    }

    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        Connection connection = null;
        List<PreparedStatement> statementList = new LinkedList<>();

        boolean output = false;
        try {
            // Apertura connessione
            connection = DriverManager.getConnection(Database.getDatabaseUrl(),Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false); // Avvia transazione

            String query1 = "INSERT INTO modello (id_categoria,id_brand,nome,descrizione)" +
                    "VALUES ('"+request.get(0).getValue()+"','"+request.get(1).getValue()+"'," +
                    "'"+request.get(2).getValue()+"','"+request.get(3).getValue()+"')";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.executeUpdate();

            String query2 = "SELECT * FROM modello WHERE (id_categoria='"+request.get(0).getValue()+"' " +
                    "&& id_brand='"+request.get(1).getValue()+"'&& nome='"+request.get(2).getValue()+"'&& descrizione='"+request.get(3).getValue()+"')";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet rs = preparedStatement2.executeQuery();

            int idModello = -1;
            if (rs.next()) {
                idModello = rs.getInt("id");
            } else {
                throw new SQLException("No image found for the provided URL");
            }

            //Inserisci i colori associati al modello
            String query3 = "INSERT INTO colore_has_modello (id_colore, id_modello) VALUES (?, ?)";
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);

            // Itera su tutti i colori e inseriscili nella tabella `colore_has_modello`
            for (int i = 4; i < request.size(); i++) {
                int idColore = Integer.parseInt(request.get(i).getValue());  // Converti in Integer
                preparedStatement3.setInt(1, idColore);
                preparedStatement3.setInt(2, idModello);
                preparedStatement3.addBatch();  // Aggiungi la query alla batch
            }

            // Esegui tutte le query in batch
            preparedStatement3.executeBatch();

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
    public boolean updateObject(int id,Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.updateElement(id,request, "modello");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"modello");
    }

    @Override
    public Optional<Modello> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"modello",new Modello());
    }

    @Override
    public List<Modello> getAllObjects() {
        return Database.getAllElements("modello",new Modello());
    }
}
