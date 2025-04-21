package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Ordine;
import utility.Database;

import java.sql.*;
import java.util.*;

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
    public Optional<Ordine> getObject(int objectId) {
        if (objectId <= 0) {
            return Optional.empty();
        }

        // 1) Carica il driver (una tantum in init, ma va bene qui)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        // 2) SQL con spazi finali in ogni riga
        String sql =
                "SELECT " +
                        "  o.*," +
                        "  d.id AS dettaglio_id," +
                        "  d.id_prodotto," +
                        "  d.quantita," +
                        "  d.codice_sconto," +
                        "  m.id AS id_modello," +
                        "  m.nome AS nome_modello," +
                        "  m.descrizione AS descrizione_modello," +
                        "  c.id AS id_categoria," +
                        "  c.nome_categoria," +
                        "  c.target," +
                        "  b.id AS id_brand," +
                        "  b.nome AS nome_brand," +
                        "  b.descrizione AS descrizione_brand," +
                        "  p.stato_pubblicazione," +
                        "  p.prezzo," +
                        "  tg.taglia_Eu," +
                        "  tg.taglia_Uk," +
                        "  tg.taglia_Us," +
                        "  i.url AS url," +
                        "  cl.nome AS nome_colore " +          // ← spazio qui
                        "FROM ordine o " +                    // ← spazio qui
                        "JOIN dettagli_ordine d ON o.id = d.id_ordine " +
                        "JOIN prodotti p        ON d.id_prodotto = p.id " +
                        "JOIN modello m         ON p.id_modello = m.id " +
                        "JOIN categoria c       ON m.id_categoria = c.id " +
                        "JOIN brand b           ON m.id_brand     = b.id " +
                        "LEFT JOIN taglie_has_prodotti thp ON p.id = thp.id_prodotto " +
                        "LEFT JOIN taglia tg    ON thp.id_taglia = tg.id " +
                        "LEFT JOIN immagini_has_prodotti ihp ON p.id = ihp.id_prodotto " +
                        "LEFT JOIN immagini i   ON ihp.id_immagine = i.id " +
                        "LEFT JOIN colore_has_prodotti chp ON p.id = chp.id_prodotto " +
                        "LEFT JOIN colore cl    ON chp.id_colore = cl.id " +
                        "WHERE o.id = ? " +
                        "ORDER BY d.id, m.id;";

        // 3) Apertura connessione e statement in try-with-resources
        try (Connection conn = DriverManager.getConnection(
                Database.getDatabaseUrl(),
                Database.getDatabaseUsername(),
                Database.getDatabasePassword());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, objectId);

            try (ResultSet rs = ps.executeQuery()) {
                // 4) Se c'è almeno una riga, converti in Ordine
                Ordine template = new Ordine();
                if (rs.next()) {
                    return template.convertDBToJava(rs);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
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
            return Collections.emptyList();
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        String sql =
                "SELECT "
                        + "  o.*, "
                        + "  d.id AS dettaglio_id, "
                        + "  d.id_prodotto, "
                        + "  d.quantita, "
                        + "  d.codice_sconto, "
                        + "  m.id AS id_modello, "
                        + "  m.nome AS nome_modello, "
                        + "  m.descrizione AS descrizione_modello, "
                        + "  c.id AS id_categoria, "
                        + "  c.nome_categoria, "
                        + "  c.target, "
                        + "  b.id AS id_brand, "
                        + "  b.nome AS nome_brand, "
                        + "  b.descrizione AS descrizione_brand, "
                        + "  p.stato_pubblicazione, "
                        + "  p.prezzo, "
                        + "  tg.taglia_Eu, "
                        + "  tg.taglia_Uk, "
                        + "  tg.taglia_Us, "
                        + "  i.url, "
                        + "  cl.nome AS nome_colore "
                        + "FROM ordine o "
                        + "JOIN dettagli_ordine d               ON o.id = d.id_ordine "
                        + "JOIN prodotti p                      ON d.id_prodotto = p.id "
                        + "JOIN modello m                       ON p.id_modello = m.id "
                        + "JOIN categoria c                     ON m.id_categoria = c.id "
                        + "JOIN brand b                         ON m.id_brand     = b.id "
                        + "LEFT JOIN taglie_has_prodotti thp    ON p.id = thp.id_prodotto "
                        + "LEFT JOIN taglia tg                  ON thp.id_taglia = tg.id "
                        + "LEFT JOIN immagini_has_prodotti ihp  ON p.id = ihp.id_prodotto "
                        + "LEFT JOIN immagini i                 ON ihp.id_immagine = i.id "
                        + "LEFT JOIN colore_has_prodotti chp    ON p.id = chp.id_prodotto "
                        + "LEFT JOIN colore cl                  ON chp.id_colore = cl.id "
                        + "WHERE o.id_utente = ? "
                        + "ORDER BY o.id, d.id, m.id;";

        List<Ordine> ordini = new LinkedList<>();

        try (
                Connection conn = DriverManager.getConnection(
                        Database.getDatabaseUrl(),
                        Database.getDatabaseUsername(),
                        Database.getDatabasePassword()
                );
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                Ordine template = new Ordine();
                while (rs.next()) {
                    template.convertDBToJava(rs)
                            .ifPresent(ordini::add);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // in caso di errore restituisco lista vuota
            ordini.clear();
        }

        return ordini;
    }
}
