package controllers;

import models.Colore;
import models.ImmaginiProdotti;
import models.Modello;
import models.Oggetti;
import models.ProdottiFull;
import models.Taglia;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            fields1.put(0,new QueryFields<>("id_categoria",(Integer) request.get(1).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_brand",(Integer) request.get(2).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("nome",(String) request.get(0).getFieldValue(), TipoVariabile.string));
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
                    fields1.put(3, new QueryFields<>("descrizione", (String) request.get(3).getFieldValue(), TipoVariabile.string));
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
                fields2.put(1,new QueryFields<>("prezzo",(Double)  request.get(4).getFieldValue(),TipoVariabile.realNumber));
                fields2.put(2,new QueryFields<>("stato_pubblicazione",(Integer) request.get(5).getFieldValue(),TipoVariabile.longNumber));
            }catch(SQLException exception){
                exception.printStackTrace();
                return false;
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
                int idColoreProdotto = Database.insertElement(connection,"colore_has_prodotti",fields3);
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

        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_categoria",(Integer) request.get(1).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_brand",(Integer) request.get(2).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("nome",(String) request.get(0).getFieldValue(), TipoVariabile.string));
            fields1.put(3,new QueryFields<>("descrizione",(String) request.get(3).getFieldValue(), TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }

        Map<Integer,QueryFields<? extends Comparable<?>>> fields3 = new HashMap<>();
        try{
            fields3.put(0,new QueryFields<>("id_prodotto",objectid, TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }

        //estrazione di tutti i colori dalla mappa che arriva dalla servlet.
        List<Colore> colori = new LinkedList<>();
        for(int index = 0;index < request.size();index++) {
            QueryFields<? extends Comparable<?>> field = request.get(index);
            if (field.getFieldName().startsWith("colore_")) {
                int idColore = (Integer) (field.getFieldValue());
                colori.add(new Colore(idColore, "unknown"));
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
            return false;
        }

        boolean success = Database.executeTransaction(connection -> {
            Optional<ProdottiFull> prodotto = Database.getElement(connection,"prodotti",objectid,new ProdottiFull());
            if (prodotto.isEmpty()){
                return false;
            }
            int idModelloOld = prodotto.get().getIdModello();

            int idModello = Database.insertElement(connection,"modello",fields1);
            if (idModello<=0){
                return false;
            }

            Map<Integer,QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
            try{
                fields2.put(0,new QueryFields<>("id_modello",idModello, TipoVariabile.longNumber));
                fields2.put(1,new QueryFields<>("prezzo",(Double) request.get(4).getFieldValue(), TipoVariabile.realNumber));
                fields2.put(2,new QueryFields<>("stato_pubblicazione",(String)  request.get(5).getFieldValue(), TipoVariabile.string));
            }catch(SQLException exception){
                exception.printStackTrace();
                return false;
            }
            boolean resultQuery2 = Database.updateElement(connection,"prodotti",objectid,fields2);
            if (!resultQuery2){
                return false;
            }

            String query3 = "DELETE FROM colore_has_prodotti WHERE id_prodotto = ?";
            boolean resultQuery3 = Database.executeGenericUpdate(connection,query3,fields3);
            if (!resultQuery3){
                return false;
            }

            //inserimento di tutti i colori nel database (associazione tra colore e prodotto).
            for(Colore colore:colori){
                Map<Integer,QueryFields<? extends Comparable<?>>> fields4 = new HashMap<>();
                try{
                    fields4.put(0,new QueryFields<>("id_colore",colore.getId(),TipoVariabile.longNumber));
                    fields4.put(1,new QueryFields<>("id_prodotto",objectid,TipoVariabile.longNumber));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int idColoreProdotto = Database.insertElement(connection,"colore_has_prodotti",fields4);
                if(idColoreProdotto <= 0){
                    return false;
                }
            }

            String query5 = "DELETE FROM taglie_has_prodotti WHERE id_prodotto = ?;";
            boolean resultQuery5 = Database.executeGenericUpdate(connection,query5,fields3);
            if (!resultQuery5){
                return false;
            }

            //inserimento di tutte le taglie con le rispettive quantità
            for(int i = 0;i < taglie.size();i++){
                Map<Integer,QueryFields<? extends Comparable<?>>> fields6 = new HashMap<>();
                try{
                    fields6.put(0,new QueryFields<>("id_taglia",taglie.get(i).getId(),TipoVariabile.longNumber));
                    fields6.put(1,new QueryFields<>("id_prodotto",objectid,TipoVariabile.longNumber));
                    fields6.put(2,new QueryFields<>("quantita",quantita.get(i),TipoVariabile.longNumber));
                }catch(SQLException exception){
                    exception.printStackTrace();
                    return false;
                }
                int tagliaProdotto = Database.insertElement(connection,"taglie_has_prodotti",fields6);
                if(tagliaProdotto <= 0){
                    return false;
                }
            }

            return Database.deleteElement(connection,"modello",idModelloOld);
        });
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
        return success;
    }

    @Override
    public boolean deleteObject(int objectid) {
        if (objectid <= 0) {
            return false;
        }

        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_prodotto",objectid, TipoVariabile.longNumber));
         }catch(SQLException exception){
            exception.printStackTrace();
            return false;
        }

        return Database.executeTransaction(connection -> {
            String query1 = "SELECT * FROM immagini_has_prodotti WHERE id_prodotto = ?";
            List<ImmaginiProdotti> immaginiProdotti = Database.executeGenericQuery(connection,query1,fields1,new ImmaginiProdotti());
            if (immaginiProdotti.isEmpty()){
                return false;
            }

            String query2 = "DELETE FROM immagini_has_prodotti WHERE id_prodotto = ?";
            boolean resultQuery2 = Database.executeGenericUpdate(connection,query2,fields1);
            if (!resultQuery2){
                return false;
            }

            for (ImmaginiProdotti immagineProdotto : immaginiProdotti){
                int idImmagineProdotto = immagineProdotto.getidImmagine();
                boolean eliminazioneImmagine = Database.deleteElement(connection,"immagini",idImmagineProdotto);
                if (!eliminazioneImmagine){
                    return false;
                }
            }

            String query3 = "DELETE FROM taglie_has_prodotti WHERE id_prodotto = ?";
            boolean resultQuery3 = Database.executeGenericUpdate(connection,query3,fields1);
            if (!resultQuery3){
                return false;
            }

            String query4 = "DELETE FROM colore_has_prodotti WHERE id_prodotto = ?";
            boolean resultQuery4 = Database.executeGenericUpdate(connection,query4,fields1);
            if (!resultQuery4){
                return false;
            }

            String query5 = "SELECT id_modello, (SELECT COUNT(*) FROM prodotti WHERE id_modello = p.id_modello) AS totale FROM prodotti p WHERE id = ?";
            List<NumeroModelli> list = Database.executeGenericQuery(connection,query5,fields1,new NumeroModelli());

            boolean resultQuery6 = Database.deleteElement(connection,"prodotti",objectid);
            if (!resultQuery6){
                return false;
            }

            for (NumeroModelli numeroModello : list){
                if (numeroModello.totale<=1){
                    boolean resultQuery7 = Database.deleteElement(connection,"modello",numeroModello.idModello);
                    if (!resultQuery7){
                        return false;
                    }
                }
            }
            return true;
        });
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
                "WHERE p.id = ?" ;

        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        List<ProdottiFull> output;
        try(Connection connection = Database.createConnection()){
            fields1.put(0,new QueryFields<>("id",objectid, TipoVariabile.longNumber));
            output = Database.executeGenericQuery(connection,query,fields1,new ProdottiFull());
        }catch (SQLException e){
            e.printStackTrace();
            output = new LinkedList<>();
        }
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
        List<ProdottiFull> output;
        try(Connection connection = Database.createConnection()){
            output = Database.executeGenericQuery(connection,query,new HashMap<>(),new ProdottiFull());
        }catch (SQLException e){
            e.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<ProdottiFull> getAllObjects() {
        return new LinkedList<>();
    }

    @Override
    public List<ProdottiFull> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }

    private static class NumeroModelli implements Oggetti<NumeroModelli>{

        private final int idModello;
        private final int totale;

        public NumeroModelli(){
            this(0,0);
        }

        public NumeroModelli(int idModello,int totale){
            this.idModello=idModello;
            this.totale=totale;
        }

        @Override
        public Optional<NumeroModelli> convertDBToJava(ResultSet rs) throws SQLException {
            int idModello1 = rs.getInt("id_modello");
            int totale1 = rs.getInt("totale");
            return Optional.of(new NumeroModelli(idModello1,totale1));
        }
    }
}
