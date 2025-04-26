package controllers;

import models.Ordine;
import models.ProdottiFull;
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
        String query =
                "SELECT " +
                        "  o.*," +
                        "  d.id AS dettaglio_id," +
                        "  d.id_prodotto," +
                        "  d.quantita," +
                        "  d.prezzo," +
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

    public List<Ordine> getObjectByUserID(int userId){
        if(userId <= 0){
            return new LinkedList<>();
        }
        String query =
                "SELECT "
                        + "  o.*, "
                        + "  d.id AS dettaglio_id, "
                        + "  d.id_prodotto, "
                        + "  d.quantita, "
                        + "  d.prezzo, "
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
        Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
        try{
            fields.put(0,new QueryFields<>("id_utente",userId,TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            return new LinkedList<>();
        }
        List<Ordine> ordini;
        try(Connection connection = Database.createConnection()){
            ordini = Database.executeGenericQuery(connection,query,fields,new Ordine());
        }catch(SQLException exception){
            exception.printStackTrace();
            ordini = new LinkedList<>();
        }
        if(ordini.isEmpty()){
            return new LinkedList<>();
        }
        List<Ordine> output = new LinkedList<>();
        for(Ordine ordine:ordini){
            if(!output.contains(ordine)){
                output.add(ordine);
            }
            List<ProdottiFull> prodotti = ordine.getProdotti();
            int index = output.indexOf(ordine);
            Ordine ordinePresente = output.get(index);
            for(ProdottiFull prodotto:prodotti){
                if(!ordinePresente.getProdotti().contains(prodotto)){
                    ordinePresente.getProdotti().add(prodotto);
                }
            }
        }
        return output;
    }
}
