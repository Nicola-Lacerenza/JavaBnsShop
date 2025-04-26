package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Colore;
import models.Modello;
import models.ProdottiFull;
import models.Taglia;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProdottiController implements Controllers<ProdottiFull> {
    public ProdottiController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        //dati per estrarre il modello dal database.
        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_categoria",request.get(1).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_brand",request.get(2).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("nome",request.get(0).getFieldValue(), TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            return -1;
        }

        //estrazione di tutti i colori dalla mappa che arriva dalla servlet.
        List<Colore> colori = new LinkedList<>();
        for(int index = 0;index < request.size();index++){
            QueryFields<? extends Comparable<?>> field = request.get(index);
            if(field.getFieldName().startsWith("colore_")){
                int idColore = (Integer)(field.getFieldValue());
                colori.add(new Colore(idColore,"unknown"));
            }
        }

        //estrazione di tutti le taglie dalla mappa che arriva dalla servlet.
        List<Taglia> taglie = new LinkedList<>();
        List<Integer> quantita = new LinkedList<>();
        for(int index = 0;index < request.size();index++){
            QueryFields<? extends Comparable<?>> field = request.get(index);
            if(field.getFieldName().startsWith("taglia_")){
                int idTaglia = (Integer)(field.getFieldValue());
                taglie.add(new Taglia(idTaglia,"unknown","unknown","unknown"));
            }
            if(field.getFieldName().startsWith("quantita_")){
                int value = (Integer)(field.getFieldValue());
                quantita.add(value);
            }
        }
        //a ogni taglia è associata una quantità quindi le liste hanno la stessa lunghezza.
        if(taglie.size() != quantita.size()){
            return -1;
        }

        //estrazione di tutti gli url delle immagini dalla mappa che arriva dalla servlet.
        List<String> urls = new LinkedList<>();
        for(int index = 0;index < request.size();index++){
            QueryFields<? extends Comparable<?>> field = request.get(index);
            if(field.getFieldName().startsWith("url")){
                urls.add(field.getFieldValue().toString());
            }
        }

        AtomicInteger idProdotto = new AtomicInteger();
        boolean success = Database.executeTransaction(connection -> {
            String query1 = "SELECT * FROM modello WHERE id_categoria = ? AND id_brand = ? AND nome = ?";
            List<Modello> modelli = Database.executeGenericQuery(connection,query1,fields1,new Modello());
            int idModello;
            if(modelli.isEmpty()){
                try {
                    fields1.put(3, new QueryFields<>("descrizione", request.get(3).getFieldValue(), TipoVariabile.string));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                idModello = Database.insertElement(connection,"modello",fields1);
                if(idModello <= 0){
                    return false;
                }
            }else{
                idModello = modelli.getFirst().getId();
                //https://chatgpt.com/c/67b8bff7-f8b0-800b-b73e-695511b0f04d
            }

            //dati per inserire il prodotto nella tabella prodotti
            Map<Integer,QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
            try{
                fields2.put(0,new QueryFields<>("id_modello",idModello,TipoVariabile.longNumber));
                fields2.put(1,new QueryFields<>("prezzo",request.get(4).getFieldValue(),TipoVariabile.realNumber));
                fields2.put(2,new QueryFields<>("stato_pubblicazione",request.get(5).getFieldValue(),TipoVariabile.string));
            }catch(SQLException exception){
                exception.printStackTrace();
                return -1;
            }
            idProdotto.set(Database.insertElement(connection,"prodotti",fields2));
            if(idProdotto.get() <= 0){
                return false;
            }

            //inserimento di tutti i colori nel database (associazione tra colore e prodotto).
            for(Colore colore:colori){
                Map<Integer,QueryFields<? extends Comparable<?>>> fields3 = new HashMap<>();
                try{
                    fields3.put(0,new QueryFields<>("id_colore",colore.getId(),TipoVariabile.longNumber));
                    fields3.put(1,new QueryFields<>("id_prodotto",idProdotto.get(),TipoVariabile.longNumber));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int idColoreProdotto = Database.insertElement(connection,"colore_has_prodotti",fields3));
                if(idColoreProdotto <= 0){
                    return false;
                }
            }

            //inserimento di tutte le taglie con le rispettive quantità
            for(int i = 0;i < taglie.size();i++){
                Map<Integer,QueryFields<? extends Comparable<?>>> fields4 = new HashMap<>();
                try{
                    fields4.put(0,new QueryFields<>("id_taglia",taglie.get(i).getId(),TipoVariabile.longNumber));
                    fields4.put(1,new QueryFields<>("id_prodotto",idProdotto.get(),TipoVariabile.longNumber));
                    fields4.put(2,new QueryFields<>("quantita",quantita.get(i),TipoVariabile.longNumber));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int tagliaProdotto = Database.insertElement(connection,"taglie_has_prodotti",fields4);
                if(tagliaProdotto <= 0){
                    return false;
                }
            }

            for(String url:urls){
                Map<Integer,QueryFields<? extends Comparable<?>>> fields5 = new HashMap<>();
                try{
                    fields5.put(0,new QueryFields<>("url",url,TipoVariabile.string));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int idImmagine = Database.insertElement(connection,"immagini",fields5);
                if(idImmagine <= 0){
                    return false;
                }
                Map<Integer,QueryFields<? extends Comparable<?>>> fields6 = new HashMap<>();
                try{
                    fields6.put(0,new QueryFields<>("id_immagine",idImmagine,TipoVariabile.longNumber));
                    fields6.put(1,new QueryFields<>("id_prodotto",idProdotto.get(),TipoVariabile.longNumber));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int idImmagineProdotto = Database.insertElement(connection,"immagini_has_prodotti",fields6);
                if(idImmagineProdotto <= 0){
                    return false;
                }
            }
            return true;
        });

        if(success){
            return idProdotto.get();
        }
        return -1;
    }

    @Override
    public boolean updateObject(int objectid,Map<Integer,QueryFields<? extends Comparable<?>>> request) {
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
            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false); // Avvia transazione


    //region ESTRAZIONE E INSERIMENTO ID MODELLO SE NON ESISTE + AGGIORNAMENTO ID MODELLO NELLA TABELLA PRODOTTI
        // ESTRAZIONE ID MODELLO

            String query1 = "SELECT id_modello FROM prodotti WHERE id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, objectid);
            ResultSet rs = preparedStatement1.executeQuery();

            int idModelloOld = -1;
            if (rs.next()) {
                idModelloOld = rs.getInt(1);
            }

        // INSERIMENTO MODELLO SE NON ESISTE + AGGIORNAMENTO ID MODELLO NELLA TABELLA PRODOTTI

            String query2 = "INSERT INTO modello (id_categoria, id_brand, nome,descrizione) " +
                    "VALUES (?, ?, ?,?) " +
                    "ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id);";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
            preparedStatement2.setInt(1, Integer.parseInt(request.get(1).getValue()));
            preparedStatement2.setInt(2, Integer.parseInt(request.get(2).getValue()));
            preparedStatement2.setString(3, request.get(0).getValue());
            preparedStatement2.setString(4, request.get(3).getValue());
            preparedStatement2.executeUpdate();

            ResultSet rs1 = preparedStatement2.getGeneratedKeys();
            int idModello = -1;
            if (rs1.next()) {
                idModello = rs1.getInt(1);
            }

            String query3 = "UPDATE prodotti SET id_modello = ? WHERE id = ?;";
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
            preparedStatement3.setInt(1, idModello);
            preparedStatement3.setInt(2, objectid);
            preparedStatement3.executeUpdate();

    //endregion

    // region AGGIORNAMENTO TABELLA PRODOTTI - CAMPI PREZZO E' STATO PUBBLICAZIONE

            String query4 = "UPDATE `prodotti` SET `prezzo`= ?,`stato_pubblicazione`= ? WHERE id= ?";
            PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
            preparedStatement4.setDouble(1, Double.parseDouble(request.get(4).getValue()));  // Assumendo che sia un prezzo numerico
            preparedStatement4.setString(2, request.get(5).getValue());
            preparedStatement4.setInt(3, objectid);  // Impostiamo l'ID del prodotto
            preparedStatement4.executeUpdate();
    //endregion

    // region ELIMINA I VECCHI COLORI E AGGIORNA CON QUELLI NUOVI

            String query5 = "DELETE FROM colore_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
            preparedStatement5.setInt(1, objectid);
            preparedStatement5.executeUpdate();

            String query6 = "INSERT INTO colore_has_prodotti (id_colore, id_prodotto) VALUES (?, ?)";
            PreparedStatement preparedStatement6 = connection.prepareStatement(query6);

            int countColore = 0;
            for (Map.Entry<Integer, RegisterServlet.RegisterFields> entry : request.entrySet()) {
                if (entry.getKey() >= 6 && entry.getValue().getKey().startsWith("colore_")) {
                    countColore++;
                }
            }

            // Itera su tutti i colori e inseriscili nella tabella `colore_has_modello`
            for (int i = 6; i < countColore + 6; i++) {
                int idColore = Integer.parseInt(request.get(i).getValue());  // Converti in Integer
                preparedStatement6.setInt(1, idColore);
                preparedStatement6.setInt(2, objectid);
                preparedStatement6.addBatch();  // Aggiungi la query alla batch
            }

            // Esegui tutte le query in batch
            preparedStatement6.executeBatch();
    //endregion

    // region ELIMINA LE VECCHIE TAGLIA E QUANTITA E AGGIORNA CON QUELLE NUOVE

            String query7 = "DELETE FROM taglie_has_prodotti WHERE id_prodotto = ?;";
            PreparedStatement preparedStatement7 = connection.prepareStatement(query7);
            preparedStatement7.setInt(1, objectid);
            preparedStatement7.executeUpdate();

            // INSERIMENTO TAGLIA E QUANTITA

            String query8 = "INSERT INTO taglie_has_prodotti (id_taglia, id_prodotto, quantita) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement8 = connection.prepareStatement(query8);
            int startTaglie = -1;
            int countTaglie = 0;

            for (Map.Entry<Integer, RegisterServlet.RegisterFields> entry : request.entrySet()) {
                String key = entry.getValue().getKey();

                // Identifica il primo indice di `taglia_X`
                if (key.startsWith("taglia_") && startTaglie == -1) {
                    startTaglie = entry.getKey();
                }

                // Conta tutte le taglie
                if (key.startsWith("taglia_")) {
                    countTaglie++;
                }
            }

            // Verifica che ci siano quantità corrispondenti
            for (int i = startTaglie; i < startTaglie + countTaglie; i++) {
                String quantitaKey = "quantita_" + (i - startTaglie);
                if (!request.containsKey(i + countTaglie)) {
                    throw new IllegalArgumentException("Quantità mancante per la taglia: " + quantitaKey);
                }
            }

            // Ora puoi iterare per inserire nel database
            for (int i = 0; i < countTaglie; i++) {
                int idTaglia = Integer.parseInt(request.get(startTaglie + i).getValue());
                int quantita = Integer.parseInt(request.get(startTaglie + countTaglie + i).getValue());

                preparedStatement8.setInt(1, idTaglia);
                preparedStatement8.setInt(2, objectid);
                preparedStatement8.setInt(3, quantita);
                preparedStatement8.addBatch();
            }

            preparedStatement8.executeBatch();
    //endregion

    //region ELIMINA LE VECCHIE IMMAGINI E AGGIORNA CON QUELLE NUOVE

           /* String query100 = "SELECT id_immagine FROM immagini_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement100 = connection.prepareStatement(query100);
            preparedStatement100.setInt(1, objectid);  // Impostiamo l'ID del prodotto
            ResultSet rs100 = preparedStatement100.executeQuery();

            List<Integer> immaginiDaEliminare = new ArrayList<>();
            while (rs100.next()) {
                immaginiDaEliminare.add(rs100.getInt("id_immagine"));
            }

            if (immaginiDaEliminare.isEmpty()) {
                throw new SQLException("No images found for the provided product");
            }

            // ELIMINA I RIFERIMENTI DALLA TABELLA IMMAGINI_HAS_PRODOTTI

            String query200 = "DELETE FROM immagini_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement200 = connection.prepareStatement(query200);
            preparedStatement200.setInt(1, objectid);
            preparedStatement200.executeUpdate();

            // ELIMINA LE IMMAGINI DALLA TABELLA DELLE IMMAGINI USANDO GLI ID IDENTIFICATI

            String query300 = "DELETE FROM immagini WHERE id = ?";
            PreparedStatement preparedStatement300 = connection.prepareStatement(query300);

            for (int idImmagine : immaginiDaEliminare) {
                preparedStatement300.setInt(1, idImmagine);
                preparedStatement300.addBatch();  // Aggiungi la query per l'eliminazione dell'immagine
            }
            preparedStatement300.executeBatch();  // Esegui tutte le query in batch

        // INSERIMENTO URL IMMAGINE

            int countUrl = 0;
            for (Map.Entry<Integer, RegisterServlet.RegisterFields> entry : request.entrySet()) {
                if (entry.getKey() >= 6 && entry.getValue().getKey().startsWith("url")) {
                    countUrl++;
                }
            }

            // Prepara la query per l'inserimento dell'immagine utilizzando RETURN_GENERATED_KEYS
            String query800 = "INSERT INTO immagini (url) VALUES (?)";
            PreparedStatement preparedStatement800 = connection.prepareStatement(query800, Statement.RETURN_GENERATED_KEYS);

            // Prepara la query per associare l'immagine al prodotto usando parametri
            String query1000 = "INSERT INTO immagini_has_prodotti (id_immagine, id_prodotto) VALUES (?, ?)";
            PreparedStatement preparedStatement1000 = connection.prepareStatement(query1000);

            for (int i = 6 + countColore; i < 6 + countColore + countUrl; i++) {
                String combinedValue = request.get(i).getValue().replace("\\", "\\\\");
                preparedStatement800.setString(1, combinedValue);
                preparedStatement800.executeUpdate();

                // Recupera il generated key dell'immagine inserita
                ResultSet generatedKeys1 = preparedStatement800.getGeneratedKeys();
                int idImmagine = -1;
                if (generatedKeys1.next()) {
                    idImmagine = generatedKeys1.getInt(1);
                } else {
                    throw new SQLException("No image found for the provided URL");
                }

                // Inserisci l'associazione tra l'immagine e il prodotto utilizzando i parametri
                preparedStatement1000.setInt(1, idImmagine);
                preparedStatement1000.setInt(2, objectid);
                preparedStatement1000.executeUpdate();
            }*/
    //endregion

    //region ELIMINO ID MODELLO VECCHIO SE NON E' PIU UTILIZZATO

            if (idModelloOld != idModello) {
                String query9 = "SELECT id FROM prodotti WHERE id_modello = ?";
                PreparedStatement preparedStatement9 = connection.prepareStatement(query9);
                preparedStatement9.setInt(1, idModelloOld);
                ResultSet rs2 = preparedStatement9.executeQuery();

                if (rs2.next()) {

                }else{
                    // Elimina il prodotto dalla tabella prodotti
                    String query10 = "DELETE FROM modello WHERE id = ?";
                    PreparedStatement preparedStatement10 = connection.prepareStatement(query10);
                    preparedStatement10.setInt(1, idModelloOld);
                    preparedStatement10.executeUpdate();
                }
            }
    //endregion

            connection.commit();
            output = true;

        } catch(SQLException e){
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

        // IDENTIFICA GLI ID DELLE IMMAGINI DA ELIMINARE

            String query1 = "SELECT id_immagine FROM immagini_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, objectid);  // Impostiamo l'ID del prodotto
            ResultSet rs = preparedStatement1.executeQuery();

            List<Integer> immaginiDaEliminare = new LinkedList<>();
            while (rs.next()) {
                immaginiDaEliminare.add(rs.getInt("id_immagine"));
            }

            if (immaginiDaEliminare.isEmpty()) {
                throw new SQLException("No images found for the provided product");
            }

        // ELIMINA I RIFERIMENTI DALLA TABELLA IMMAGINI_HAS_PRODOTTI

            String query2 = "DELETE FROM immagini_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, objectid);
            preparedStatement2.executeUpdate();

        // ELIMINA LE IMMAGINI DALLA TABELLA DELLE IMMAGINI USANDO GLI ID IDENTIFICATI

            String query3 = "DELETE FROM immagini WHERE id = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);

            for (int idImmagine : immaginiDaEliminare) {
                preparedStatement3.setInt(1, idImmagine);
                preparedStatement3.addBatch();  // Aggiungi la query per l'eliminazione dell'immagine
            }
            preparedStatement3.executeBatch();  // Esegui tutte le query in batch

        // ELIMINA RIFERIMENTI TAGLIE_HAS_PRODOTTI

            String query4 = "DELETE FROM taglie_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
            preparedStatement4.setInt(1, objectid);
            preparedStatement4.executeUpdate();

        // ELIMINA RIFERIMENTI COLORE_HAS_PRODOTTI

            String query5 = "DELETE FROM colore_has_prodotti WHERE id_prodotto = ?";
            PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
            preparedStatement5.setInt(1, objectid);
            preparedStatement5.executeUpdate();

        // ESTRAZIONE ID MODELLO + ELIMINAZIONE PRODOTTO + MODELLO(SE UNICO)

            String query10 = "SELECT id_modello, (SELECT COUNT(*) FROM prodotti WHERE id_modello = p.id_modello) AS totale FROM prodotti p WHERE id = ?";
            PreparedStatement preparedStatement10 = connection.prepareStatement(query10);
            preparedStatement10.setInt(1, objectid);
            ResultSet rs10 = preparedStatement10.executeQuery();

            int idModello = -1;
            int count = 0;

            if (rs10.next()) {
                idModello = rs10.getInt("id_modello");
                count = rs10.getInt("totale");
            }

            // Elimina il prodotto dalla tabella prodotti
            String query6 = "DELETE FROM prodotti WHERE id = ?";
            PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
            preparedStatement6.setInt(1, objectid);
            preparedStatement6.executeUpdate();

            if (count<=1){
                // Elimina il prodotto dalla tabella prodotti
                String query8 = "DELETE FROM modello WHERE id = ?";
                PreparedStatement preparedStatement8 = connection.prepareStatement(query8);
                preparedStatement8.setInt(1, idModello);
                preparedStatement8.executeUpdate();
            }

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
    public Optional<ProdottiFull> getObject(int objectid) {
        if (objectid <= 0) {
            return Optional.empty();
        }
        List<ProdottiFull> list = getFullObject(objectid);
        if (list.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    public List<ProdottiFull> getFullObject(int objectid){
        String query = "SELECT p.id,\n" +
                "       m.id AS id_modello,\n" +
                "       m.nome AS nome_modello,\n" +
                "       m.descrizione AS descrizione_modello,\n" +
                "       c.id AS id_categoria,\n" +
                "       c.nome_categoria,\n" +
                "       c.target,\n" +
                "       b.id AS id_brand,\n" +
                "       b.nome AS nome_brand,\n" +
                "       b.descrizione AS descrizione_brand,\n" +
                "       p.prezzo,\n" +
                "       p.stato_pubblicazione,\n" +
                "       taglia.taglia_Eu,\n" +
                "       taglia.taglia_Uk,\n" +
                "       taglia.taglia_Us,\n" +
                "       tp.quantita,\n" +
                "       immagini.url,\n" +
                "       colore.nome AS nome_colore\n" +
                "FROM prodotti p\n" +
                "JOIN modello m ON p.id_modello = m.id\n" +
                "JOIN categoria c ON m.id_categoria = c.id\n" +
                "JOIN brand b ON m.id_brand = b.id\n" +
                "JOIN taglie_has_prodotti tp ON tp.id_prodotto = p.id\n" +
                "JOIN taglia ON taglia.id = tp.id_taglia\n" +
                "JOIN immagini_has_prodotti ip ON ip.id_prodotto = p.id\n" +
                "JOIN immagini ON immagini.id = ip.id_immagine\n" +
                "JOIN colore_has_prodotti cp ON cp.id_prodotto = p.id\n" +
                "JOIN colore ON colore.id = cp.id_colore\n" +
                "WHERE p.ID =" + objectid;
        List<ProdottiFull> output = Database.executeGenericQuery("prodotti", new ProdottiFull(), query);
        return output;
    }

    public List<ProdottiFull> getAllProducts(){
        String query="SELECT\n" +
                "                    prodotti.id,\n" +
                "                    modello.id AS id_modello,\n" +
                "                    modello.nome AS nome_modello,\n" +
                "                    modello.descrizione AS descrizione_modello,\n" +
                "                    categoria.id AS id_categoria,\n" +
                "                    categoria.nome_categoria,\n" +
                "                    categoria.target,\n" +
                "                    brand.id AS id_brand,\n" +
                "                    brand.nome AS nome_brand,\n" +
                "                    brand.descrizione AS descrizione_brand,\n" +
                "                    prodotti.prezzo,\n" +
                "                    prodotti.stato_pubblicazione,\n" +
                "                    taglia.taglia_Eu,\n" +
                "                    taglia.taglia_Uk,\n" +
                "                    taglia.taglia_Us,\n" +
                "                    tp.quantita,\n" +
                "                    immagini.url,\n" +
                "                    colore.nome AS nome_colore\n" +
                "                FROM prodotti\n" +
                "                INNER JOIN modello ON prodotti.id_modello = modello.id\n" +
                "                INNER JOIN immagini_has_prodotti ON prodotti.id = immagini_has_prodotti.id_prodotto\n" +
                "                INNER JOIN immagini ON immagini.id = immagini_has_prodotti.id_immagine \n" +
                "                INNER JOIN brand ON modello.id_brand = brand.id\n" +
                "                INNER JOIN categoria ON modello.id_categoria = categoria.id\n" +
                "                INNER JOIN taglie_has_prodotti tp ON tp.id_prodotto = prodotti.id\n" +
                "                INNER JOIN taglia ON taglia.id = tp.id_taglia\n" +
                "                INNER JOIN colore_has_prodotti cp ON cp.id_prodotto = prodotti.id\n" +
                "                INNER JOIN colore ON colore.id = cp.id_colore";

        List<ProdottiFull> tmp = Database.executeGenericQuery("prodotti",new ProdottiFull(),query);
        return tmp;
    }

    @Override
    public List<ProdottiFull> getAllObjects() {
        return new LinkedList<>();
    }

    @Override
    public List<ProdottiFull> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}
