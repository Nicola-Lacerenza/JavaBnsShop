package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Ordine;
import utility.Database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrdineController implements Controllers<Ordine>{


    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<Ordine> getObject(int objectid) {

        /*SELECT
  o.*,
  d.id              AS dettaglio_id,
  d.id_prodotto,
  d.quantita,
  d.codice_sconto,
  m.id              AS modello_id,
  m.nome            AS nome_modello,
  m.descrizione     AS descrizione_modello,
  c.id,
  c.nome_categoria,
  b.id,
  b.nome            AS nome_brand,
  b.descrizione     AS descrizione_brand,
  p.stato_pubblicazione,
  p.prezzo,
  tg.taglia_Eu,
  tg.taglia_Uk,
  tg.taglia_Us,
  i.url,
  cl.nome           AS nome_colore
FROM ordine o
JOIN dettagli_ordine d               ON o.id = d.id_ordine
JOIN prodotti p                      ON d.id_prodotto = p.id
JOIN modello m                       ON p.id_modello = m.id
JOIN categoria c                     ON m.id_categoria = c.id
JOIN brand b                         ON m.id_brand     = b.id
LEFT JOIN taglie_has_prodotti thp   ON p.id = thp.id_prodotto
LEFT JOIN taglia tg                 ON thp.id_taglia = tg.id
LEFT JOIN immagini_has_prodotti ihp ON p.id = ihp.id_prodotto
LEFT JOIN immagini i                ON ihp.id_immagine = i.id
LEFT JOIN colore_has_prodotti chp   ON p.id = chp.id_prodotto
LEFT JOIN colore cl                 ON chp.id_colore = cl.id
WHERE o.id = 2
ORDER BY d.id, m.id;*/
        return Optional.empty();
    }

    @Override
    public List<Ordine> getAllObjects() {
        return null;
    }

    @Override
    public List<Ordine> executeQuery(String query) {
        return null;
    }

    public List<Ordine> getObjectByUserID(int userID) {
        if (userID <= 0) {
            return new LinkedList<>();
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        Connection connection = null;
        List<Ordine> ordini = new LinkedList<>();

        try {

            // APERTURA CONNESSIONE

            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false); // Avvia transazione

            // IDENTIFICA GLI ID DELLE IMMAGINI DA ELIMINARE

            String query1 = "SELECT * FROM ordine WHERE id_utente = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, userID);
            ResultSet rs = preparedStatement1.executeQuery();
            Ordine ordineTemplate = new Ordine();
            while (rs.next()){
                Optional<Ordine> ordine = ordineTemplate.convertDBToJava(rs);
                if (ordine.isPresent()){
                    ordini.add(ordine.get());
                }
            }
            connection.commit();
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
            ordini.clear();
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
        return ordini;
        }

}
