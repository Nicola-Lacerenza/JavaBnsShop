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

        // INSERIMENTO MODELLO

            String query1 = "INSERT INTO modello (id_categoria,id_brand,nome,descrizione)" +
                    "VALUES ('"+request.get(1).getValue()+"','"+request.get(2).getValue()+"'," +
                    "'"+request.get(0).getValue()+"','"+request.get(3).getValue()+"')";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.executeUpdate();

            String query2 = "SELECT * FROM modello WHERE (id_categoria='"+request.get(1).getValue()+"' " +
                    "&& id_brand='"+request.get(2).getValue()+"'&& nome='"+request.get(0).getValue()+"'&& descrizione='"+request.get(3).getValue()+"')";
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

            int countColore = 0;
            for (Map.Entry<Integer, RegisterServlet.RegisterFields> entry : request.entrySet()) {
                if (entry.getKey() >= 8 && entry.getValue().getKey().startsWith("colore_")) {
                    countColore++;
                }
            }

            // Itera su tutti i colori e inseriscili nella tabella `colore_has_modello`
            for (int i = 8; i < countColore+8; i++) {
                int idColore = Integer.parseInt(request.get(i).getValue());  // Converti in Integer
                preparedStatement3.setInt(1, idColore);
                preparedStatement3.setInt(2, idModello);
                preparedStatement3.addBatch();  // Aggiungi la query alla batch
            }


            // Esegui tutte le query in batch
            preparedStatement3.executeBatch();

        // INSERIMENTO PRODOTTO

            String query4 = "INSERT INTO prodotti (id_modello,prezzo,stato_pubblicazione)" + "VALUES ('"+idModello+"','"+request.get(4).getValue()+"'," +
                    "'"+request.get(5).getValue()+"')";
            PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
            preparedStatement4.executeUpdate();

            // ESTRAZIONE ID PRODOTTO
            String query5 ="SELECT * FROM prodotti WHERE (id_modello='"+idModello+"' " +
                    "&& prezzo='"+request.get(4).getValue()+"'&& stato_pubblicazione='"+request.get(5).getValue()+"')";
            PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
            ResultSet rs1 = preparedStatement5.executeQuery();

            int idProdotto = -1;
            if (rs1.next()) {
                idProdotto = rs1.getInt("id");
            } else {
                throw new SQLException("No image found for the provided URL");
            }

        // INSERIMENTO TAGLIA E QUANTITA

            String query9 = "INSERT INTO taglie_has_prodotti (id_taglia,id_prodotto,quantita) VALUES ('"+request.get(6).getValue()+"','"+idProdotto+"'," +
                    "'"+request.get(7).getValue()+"')";
            PreparedStatement preparedStatement9 = connection.prepareStatement(query9);
            preparedStatement9.executeUpdate();

        // INSERIMENTO URL IMMAGINE

            String query6 = "INSERT INTO immagini (url) VALUES (?)";
            PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
            for (int i = 8+countColore; i < request.size(); i++) {

                String combinedValue = request.get(i).getValue().replace("\\", "\\\\") + idProdotto;
                preparedStatement6.setString(1, combinedValue);
                preparedStatement6.executeUpdate();

                //ESTRAZIONE  ID IMMAGINE
                String query7 = "SELECT * FROM immagini WHERE url = ?";
                PreparedStatement preparedStatement7 = connection.prepareStatement(query7);
                preparedStatement7.setString(1, combinedValue); // Usa il valore originale senza ulteriori modifiche
                ResultSet rs2 = preparedStatement7.executeQuery();

                int idImmagine = -1;
                if (rs2.next()) {
                    idImmagine = rs2.getInt("id");
                } else {
                    throw new SQLException("No image found for the provided URL");
                }

                //INSERIMENTO IMMAGINE ASSOCIATA AL PRODOTTO
                String query8 = "INSERT INTO immagini_has_prodotti (id_immagine, id_prodotto) VALUES ('"+idImmagine+"', '"+idProdotto+"')";
                PreparedStatement preparedStatement8 = connection.prepareStatement(query8);
                preparedStatement8.executeUpdate();

            }

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
