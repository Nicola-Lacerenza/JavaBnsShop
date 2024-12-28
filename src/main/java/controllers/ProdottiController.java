package controllers;

import bnsshop.bnsshop.RegisterServlet;
import models.Colore;
import models.Oggetti;
import models.Prodotti;
import org.json.JSONObject;
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
                if (entry.getKey() >= 6 && entry.getValue().getKey().startsWith("colore_")) {
                    countColore++;
                }
            }

            // Itera su tutti i colori e inseriscili nella tabella `colore_has_modello`
            for (int i = 6; i < countColore+6; i++) {
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

            String query9 = "INSERT INTO taglie_has_prodotti (id_taglia, id_prodotto, quantita) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement9 = connection.prepareStatement(query9);
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

                preparedStatement9.setInt(1, idTaglia);
                preparedStatement9.setInt(2, idProdotto);
                preparedStatement9.setInt(3, quantita);
                preparedStatement9.addBatch();
            }

            preparedStatement9.executeBatch();

        // INSERIMENTO URL IMMAGINE

            int countUrl = 0;
            for (Map.Entry<Integer, RegisterServlet.RegisterFields> entry : request.entrySet()) {
                if (entry.getKey() >= 6 && entry.getValue().getKey().startsWith("url")) {
                    countUrl++;
                }
            }
            String query6 = "INSERT INTO immagini (url) VALUES (?)";
            PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
            for (int i = 6+countColore; i < 6+countColore+countUrl; i++) {

                String combinedValue = request.get(i).getValue().replace("\\", "\\\\");
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

        String query3 = "DELETE FROM immagini_has_prodotti WHERE id_prodotto ="+objectid;
        String query2 = " DELETE FROM immagini WHERE id IN (\n" +
                "                SELECT id_immagine FROM immagini_has_prodotti WHERE id_prodotto ="+objectid;
        String query4 = "DELETE FROM prodotti WHERE id ="+objectid;
        String query1 = "DELETE FROM taglie_has_prodotti WHERE id_prodotto ="+objectid;


        List<String> queries = new LinkedList<>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);
        queries.add(query4);
        return Database.executeQueries(queries);
    }

    @Override
    public Optional<Prodotti> getObject(int objectid) {
        if (objectid<=0){
            return Optional.empty();
        }
        return Database.getElement(objectid,"prodotti",new Prodotti());
    }

    public List<ResultProdotti> getAllProducts(){
        String query="SELECT\n" +
                "                    prodotti.id,\n" +
                "                    prodotti.prezzo,\n" +
                "                    prodotti.stato_pubblicazione,\n" +
                "                    modello.nome AS nome_modello,\n" +
                "                    modello.descrizione AS descrizione_modello,\n" +
                "                    categoria.nome_categoria,\n" +
                "                    brand.nome AS nome_brand,\n" +
                "                    brand.descrizione AS descrizione_brand,\n" +
                "                    immagini.url\n" +
                "                FROM prodotti\n" +
                "                INNER JOIN modello ON prodotti.id_modello = modello.id\n" +
                "                INNER JOIN immagini_has_prodotti ON prodotti.id = immagini_has_prodotti.id_prodotto\n" +
                "                INNER JOIN immagini ON immagini.id = immagini_has_prodotti.id_immagine \n" +
                "                INNER JOIN brand ON modello.id_brand = brand.id\n" +
                "                INNER JOIN categoria ON modello.id_categoria = categoria.id;\n" +
                "                ";

        List<ResultProdotti> tmp = Database.executeGenericQuery("prodotti",new ResultProdotti(),query);
        return tmp;
    }
    @Override
    public List<Prodotti> getAllObjects() {
        return new LinkedList<>();
    }

    public static class ResultProdotti implements Oggetti<ResultProdotti> {
        private  int id;
        private  double prezzo;
        private  int statoPubblicazione;
        private  String nomeModello;
        private  String descrizioneModello;
        private  String nomeCategoria;
        private  String nomeBrand;
        private  String descrizioneBrand;
        private  String url;

        public ResultProdotti(int id, double prezzo, int statoPubblicazione, String nomeModello, String descrizioneModello, String nomeCategoria, String nomeBrand, String descrizioneBrand,String url) {
            this.id = id;
            this.prezzo = prezzo;
            this.statoPubblicazione = statoPubblicazione;
            this.nomeModello = nomeModello;
            this.descrizioneModello = descrizioneModello;
            this.nomeCategoria = nomeCategoria;
            this.nomeBrand = nomeBrand;
            this.descrizioneBrand = descrizioneBrand;
            this.url = url;
        }

        public ResultProdotti(){
            this(0,0,0,"","","","","","");
        }
        @Override
        public ResultProdotti createObject() {
            return new ResultProdotti();
        }

        @Override
        public Optional<ResultProdotti> convertDBToJava(ResultSet rs) {
            int id,statoPubblicazione;
            double prezzo;
            String nomeModello,descrizioneModello,nomeCategoria,nomeBrand,descrizioneBrand,url;
            try {
                id = rs.getInt("id");
                prezzo = rs.getDouble("prezzo");
                statoPubblicazione = rs.getInt("stato_pubblicazione");
                nomeModello = rs.getString("nome_modello");
                descrizioneModello = rs.getString("descrizione_modello");
                nomeCategoria = rs.getString("nome_categoria");
                nomeBrand = rs.getString("nome_brand");
                descrizioneBrand = rs.getString("descrizione_brand");
                url = rs.getString("url");
            }catch (SQLException e){
                e.printStackTrace();
                return Optional.empty();
            }
            ResultProdotti output = new ResultProdotti(id,prezzo,statoPubblicazione,nomeModello,descrizioneModello,nomeCategoria,nomeBrand,descrizioneBrand,url);
            return Optional.of(output);
        }
        @Override
        public String toString() {
            JSONObject output = new JSONObject();
            output.put("id",id);
            output.put("prezzo",prezzo);
            output.put("stato_pubblicazione",statoPubblicazione);
            output.put("nome_modello",nomeModello);
            output.put("descrizione_modello",descrizioneModello);
            output.put("nome_categoria",nomeCategoria);
            output.put("nome_brand",nomeBrand);
            output.put("descrizione_brand",descrizioneBrand);
            output.put("url",url);
            return output.toString(4);
        }
    }

}
