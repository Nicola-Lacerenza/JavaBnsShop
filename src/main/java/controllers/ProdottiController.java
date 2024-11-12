package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Prodotti;
import utility.Database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdottiController implements Controllers<Prodotti> {

    public ProdottiController(){
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

            String query1 = "INSERT INTO prodotti (id_modello,id_taglia,prezzo,quantita,stato_pubblicazione)" +
                    "VALUES ('"+request.get(1).getValue()+"','"+request.get(2).getValue()+"',," +
                    "'"+request.get(4).getValue()+"','"+request.get(5).getValue()+"','"+request.get(6).getValue()+"')";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.executeUpdate();

            String query2 = "SELECT * FROM prodotti WHERE (id_modello='"+request.get(1).getValue()+"' " +
                    "&& id_taglia='"+request.get(2).getValue()+"'&& prezzo='"+request.get(3).getValue()+"'&& quantita='"+request.get(4).getValue()+"'" +
                    "&& stato_pubblicazione='"+request.get(5).getValue()+"')";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet rs = preparedStatement2.executeQuery();


            int idProdotto = -1;
            if (rs.next()) {
                idProdotto = rs.getInt("id");
            } else {
                throw new SQLException("No image found for the provided URL");
            }

            //Inserisci i colori associati al modello
            String query3 = "INSERT INTO immagini_has_prodotti (id_immagine, id_prodotto) VALUES (?, ?)";
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);

            // Itera su tutti i colori e inseriscili nella tabella `colore_has_modello`
            for (int i = 4; i < request.size(); i++) {
                int idImmagine = Integer.parseInt(request.get(i).getValue());  // Converti in Integer
                preparedStatement3.setInt(1, idImmagine);
                preparedStatement3.setInt(2, idProdotto);
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
        return Database.updateElement(id,request, "prodotti");
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }
        return Database.deleteElement(objectid,"prodotti");
    }

    @Override
    public Optional<Prodotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"prodotti",new Prodotti());
    }

    @Override
    public List<Prodotti> getAllObjects() {
        return Database.getAllElements("prodotti",new Prodotti());
    }


}
