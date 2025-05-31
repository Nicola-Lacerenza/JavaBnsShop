package controllers;

import models.Ordine;
import models.ProdottiFull;
import models.Taglia;
import models.TaglieProdotti;
import utility.Database;
import utility.DateManagement;
import utility.QueryFields;
import utility.TipoVariabile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrdineController implements Controllers<Ordine>{
    public OrdineController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return -1;
    }

    @Override
    public boolean updateObject(int id,Map<Integer, QueryFields<? extends Comparable<?>>> request) {
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
        String query = """
                        SELECT
                        o.*,
                        d.id AS dettaglio_id,
                        d.id_prodotto,
                        d.quantita,
                        d.prezzo,
                        m.id AS id_modello,
                        m.nome AS nome_modello,
                        m.descrizione AS descrizione_modello,
                        c.id AS id_categoria,
                        c.nome_categoria,
                        c.target,
                        b.id AS id_brand,
                        b.nome AS nome_brand,
                        b.descrizione AS descrizione_brand,
                        p.stato_pubblicazione,
                        p.prezzo,
                        tg.taglia_Eu,
                        tg.taglia_Uk,
                        tg.taglia_Us,
                        i.url AS url,
                        cl.nome AS nome_colore
                        FROM ordine o
                        JOIN dettagli_ordine d ON o.id = d.id_ordine
                        JOIN prodotti p        ON d.id_prodotto = p.id
                        JOIN modello m         ON p.id_modello = m.id
                        JOIN categoria c       ON m.id_categoria = c.id
                        JOIN brand b           ON m.id_brand     = b.id
                        LEFT JOIN taglie_has_prodotti thp ON p.id = thp.id_prodotto
                        LEFT JOIN taglia tg    ON thp.id_taglia = tg.id
                        LEFT JOIN immagini_has_prodotti ihp ON p.id = ihp.id_prodotto
                        LEFT JOIN immagini i   ON ihp.id_immagine = i.id
                        LEFT JOIN colore_has_prodotti chp ON p.id = chp.id_prodotto
                        LEFT JOIN colore cl    ON chp.id_colore = cl.id
                        WHERE o.id = ? AND d.reso_effettuabile = TRUE
                        ORDER BY d.id, m.id;""";
        Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
        try{
            fields.put(0,new QueryFields<>("id",objectId,TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        List<Ordine> ordini;
        try(Connection connection = Database.createConnection()){
            ordini = Database.executeGenericQuery(connection,query,fields,new Ordine());
        }catch(SQLException exception){
            exception.printStackTrace();
            ordini = new LinkedList<>();
        }
        if(ordini.isEmpty()){
            return Optional.empty();
        }
        List<Ordine> output = new LinkedList<>();
        for(Ordine ordine:ordini){
            if(!output.contains(ordine)){
                output.add(ordine);
            }
            List<ProdottiFull> prodotti = ordine.getProdotti();
            int ordinePresente = output.indexOf(ordine);
            for(ProdottiFull prodotto:prodotti){
                if(!output.get(ordinePresente).getProdotti().contains(prodotto)){
                    output.get(ordinePresente).getProdotti().add(prodotto);
                }
            }
        }
        if(output.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(output.getFirst());
    }

    @Override
    public List<Ordine> getAllObjects() {
        return new LinkedList<>();
    }

    @Override
    public List<Ordine> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }

    public List<Ordine> getObjectByUserID(int userId) {
        if (userId <= 0) {
            return new LinkedList<>();
        }

        String sql =
                "SELECT "
                        + "  o.id             AS ordine_id, "
                        + "  o.id_utente, "
                        + "  o.id_pagamento, "
                        + "  o.id_indirizzo, "
                        + "  o.stato_ordine, "
                        + "  o.data_creazione_ordine, "
                        + "  o.data_aggiornamento_stato_ordine, "
                        + "  o.importo, "
                        + "  o.valuta, "
                        + "  o.locale_utente, "
                        + "  d.id             AS dettaglio_id, "
                        + "  d.id_prodotto, "
                        + "  d.quantita, "
                        + "  d.prezzo         AS prezzo_dettaglio, "
                        + "  m.id             AS id_modello, "
                        + "  m.nome           AS nome_modello, "
                        + "  m.descrizione    AS descrizione_modello, "
                        + "  c.id             AS id_categoria, "
                        + "  c.nome_categoria, "
                        + "  c.target, "
                        + "  b.id             AS id_brand, "
                        + "  b.nome           AS nome_brand, "
                        + "  b.descrizione    AS descrizione_brand, "
                        + "  p.stato_pubblicazione, "
                        + "  p.prezzo         AS prezzo_prodotto, "
                        + "  tg.taglia_Eu, "
                        + "  tg.taglia_Uk, "
                        + "  tg.taglia_Us, "
                        + "  i.url, "
                        + "  cl.nome          AS nome_colore "
                        + "FROM ordine o "
                        + "JOIN dettagli_ordine d              ON o.id = d.id_ordine "
                        + "JOIN prodotti p                     ON d.id_prodotto = p.id "
                        + "JOIN modello m                      ON p.id_modello = m.id "
                        + "JOIN categoria c                    ON m.id_categoria = c.id "
                        + "JOIN brand b                        ON m.id_brand     = b.id "
                        + "LEFT JOIN taglie_has_prodotti thp   ON p.id = thp.id_prodotto "
                        + "LEFT JOIN taglia tg                 ON thp.id_taglia = tg.id "
                        + "LEFT JOIN immagini_has_prodotti ihp ON p.id = ihp.id_prodotto "
                        + "LEFT JOIN immagini i                ON ihp.id_immagine = i.id "
                        + "LEFT JOIN colore_has_prodotti chp   ON p.id = chp.id_prodotto "
                        + "LEFT JOIN colore cl                 ON chp.id_colore = cl.id "
                        + "WHERE o.id_utente = ? AND d.reso_effettuabile = TRUE "
                        + "ORDER BY o.id, d.id, tg.taglia_Eu;";

        List<Ordine> result = new LinkedList<>();

        try (Connection conn = Database.createConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {

                // 1) Mappa ordineId -> Ordine
                Map<Integer, Ordine> ordineMap = new LinkedHashMap<>();
                // 2) Per ogni ordine, mappa chiave(prodId–tagEu) -> ProdottiFull
                Map<Integer, Map<String, ProdottiFull>> prodMapByOrdine = new HashMap<>();

                while (rs.next()) {
                    int ordineId = rs.getInt("ordine_id");

                    // --- Estrai o crea Ordine ---
                    Ordine ordine = ordineMap.get(ordineId);
                    if (ordine == null) {
                        ordine = new Ordine(
                                ordineId,
                                rs.getInt("id_utente"),
                                rs.getInt("id_pagamento"),
                                rs.getInt("id_indirizzo"),
                                rs.getString("stato_ordine"),
                                DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_creazione_ordine")),
                                DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_aggiornamento_stato_ordine")),
                                rs.getDouble("importo"),
                                rs.getString("valuta"),
                                rs.getString("locale_utente"),
                                new LinkedList<>()
                        );
                        ordineMap.put(ordineId, ordine);
                        prodMapByOrdine.put(ordineId, new LinkedHashMap<>());
                    }

                    // --- Estrai chiave prodotto+taglia ---
                    int idProdotto = rs.getInt("id_prodotto");
                    String tagEu   = rs.getString("taglia_Eu");
                    String key     = idProdotto + "-" + tagEu;

                    Map<String, ProdottiFull> prodMap = prodMapByOrdine.get(ordineId);
                    ProdottiFull p = prodMap.get(key);

                    // --- Se non esiste, crealo e aggiungilo ---
                    if (p == null) {
                        // Taglia e quantità
                        List<ProdottiFull.ProdottiTaglieEstratte> taglieList = new LinkedList<>();
                        taglieList.add(new ProdottiFull.ProdottiTaglieEstratte(
                                new Taglia(0, tagEu, rs.getString("taglia_Uk"), rs.getString("taglia_Us")),
                                new TaglieProdotti(0, 0, idProdotto, rs.getInt("quantita"))
                        ));

                        // URL e colore
                        List<String> urls   = new LinkedList<>();
                        if (rs.getString("url") != null) {
                            urls.add(rs.getString("url"));
                        }
                        List<String> colori = new LinkedList<>();
                        if (rs.getString("nome_colore") != null) {
                            colori.add(rs.getString("nome_colore"));
                        }

                        // Nuovo ProdottiFull
                        p = new ProdottiFull(
                                idProdotto,
                                rs.getInt("id_modello"),
                                rs.getString("nome_modello"),
                                rs.getString("descrizione_modello"),
                                rs.getInt("id_categoria"),
                                rs.getString("nome_categoria"),
                                rs.getString("target"),
                                rs.getInt("id_brand"),
                                rs.getString("nome_brand"),
                                rs.getString("descrizione_brand"),
                                rs.getInt("stato_pubblicazione"),
                                rs.getDouble("prezzo_prodotto"),
                                taglieList,
                                urls,
                                colori
                        );

                        // Memorizza e inserisci nella lista dell'ordine
                        prodMap.put(key, p);
                        ordine.getProdotti().add(p);
                    }
                    // Altrimenti: questo prodotto+taglia è già stato aggiunto, skip
                }

                // 3) Raccogli tutti gli ordini
                result.addAll(ordineMap.values());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
