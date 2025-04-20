package models;

import org.json.JSONObject;
import utility.DateManagement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Ordine implements Oggetti<Ordine>{
    private final int id;
    private final int idUtente;
    private final int idPagamento;
    private final int idIndirizzo;
    private final String statoOrdine;
    private final Calendar dataCreazioneOrdine;
    private final Calendar dataAggiornamentoStatoOrdine;
    private final double importo;
    private final String valuta;
    private final String localeUtente;
    private final List<ProdottiFull> prodotti;


    public Ordine(int id, int idUtente, int idPagamento, int idIndirizzo, String statoOrdine, Calendar dataCreazioneOrdine,Calendar dataAggiornamentoStatoOrdine,double importo,String valuta,String localeUtente,List<ProdottiFull> listProdotti) {
        this.id = id;
        this.idUtente = idUtente;
        this.idPagamento = idPagamento;
        this.idIndirizzo = idIndirizzo;
        this.statoOrdine = statoOrdine;
        this.dataCreazioneOrdine = dataCreazioneOrdine;
        this.dataAggiornamentoStatoOrdine = dataAggiornamentoStatoOrdine;
        this.importo = importo;
        this.valuta = valuta;
        this.localeUtente = localeUtente;
        this.prodotti = listProdotti;
    }

    public Ordine(){
        this(0,0,0,0,"",Calendar.getInstance(),Calendar.getInstance(),0.0,"","",new LinkedList<>());
    }

    public int getId() {
        return id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public String getStatoOrdine() {
        return statoOrdine;
    }

    public Calendar getDataCreazioneOrdine() {
        return DateManagement.copyCalendar(dataCreazioneOrdine);
    }

    public Calendar getDataAggiornamentoStatoOrdine() {
        return DateManagement.copyCalendar(dataAggiornamentoStatoOrdine);
    }

    public double getImporto() {
        return importo;
    }

    public String getValuta() {
        return valuta;
    }

    public String getLocale_utente() {
        return localeUtente;
    }
    public List<ProdottiFull> getProdotti() {
        return prodotti;
    }

    @Override
    public Optional<Ordine> convertDBToJava(ResultSet rs) {
        try {
            // 1) Leggi i campi "ordine" dalla riga corrente
            int orderId = rs.getInt("id");
            int idUtente = rs.getInt("id_utente");
            int idPagamento = rs.getInt("id_pagamento");
            int idIndirizzo = rs.getInt("id_indirizzo");
            String statoOrdine = rs.getString("stato_ordine");
            Calendar dataCreazione = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_creazione_ordine"));
            Calendar dataAggiornamento = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_aggiornamento_stato_ordine"));
            double importo = rs.getDouble("importo");
            String valuta = rs.getString("valuta");
            String localeUtente = rs.getString("locale_utente");

            // 2) Prepara la lista dei prodotti
            List<ProdottiFull> listaProdotti = new LinkedList<>();

            // 3) Ciclo do–while: ogni riga con lo stesso orderId è un prodotto diverso
            do {
                if (rs.getInt("id") != orderId) {
                    // se l'id ordine cambia, torniamo indietro e usciamo
                    rs.previous();
                    break;
                }

                // 3a) Leggi i campi “prodotto” dalla riga corrente
                int idProdFull =      rs.getInt("id");
                int idModello =       rs.getInt("id_modello");
                String nomeModello =  rs.getString("nome_modello");
                String descModello =  rs.getString("descrizione_modello");
                int idCategoria =     rs.getInt("id_categoria");
                String nomeCategoria =rs.getString("nome_categoria");
                String target =       rs.getString("target");
                int idBrand =         rs.getInt("id_brand");
                String nomeBrand =    rs.getString("nome_brand");
                String descBrand =    rs.getString("descrizione_brand");
                int statoPub =        rs.getInt("stato_pubblicazione");
                double prezzo =       rs.getDouble("prezzo");

                // 3b) Prepara le liste di taglie, url e colori per questo prodotto
                List<ProdottiFull.ProdottiTaglieEstratte> taglie = new LinkedList<>();
                List<String> urls    = new LinkedList<>();
                List<String> colori  = new LinkedList<>();

                String tagEu = rs.getString("taglia_Eu");
                String tagUk = rs.getString("taglia_Uk");
                String tagUs = rs.getString("taglia_Us");
                int quantita = Integer.parseInt(rs.getString("quantita"));
                String url1 = rs.getString("url");
                String col1 = rs.getString("nome_colore");

                taglie.add(new ProdottiFull.ProdottiTaglieEstratte(
                        new Taglia(0, tagEu, tagUk, tagUs),
                        new TaglieProdotti(0, 0, idProdFull, quantita)
                ));
                urls.add(url1);
                colori.add(col1);

                // 3c) Istanzia ProdottiFull col costruttore corretto (15 parametri)
                ProdottiFull prod = new ProdottiFull(
                        idProdFull,    // 1) id
                        idModello,     // 2) idModello
                        nomeModello,   // 3)
                        descModello,   // 4)
                        idCategoria,   // 5)
                        nomeCategoria, // 6)
                        target,        // 7)
                        idBrand,       // 8)
                        nomeBrand,     // 9)
                        descBrand,     // 10)
                        statoPub,      // 11)
                        prezzo,        // 12)
                        taglie,        // 13) List<ProdottiTaglieEstratte>
                        urls,          // 14) List<String> url
                        colori         // 15) List<String> nomeColore
                );
                listaProdotti.add(prod);

            } while (rs.next());

            // 4) Costruisci e ritorna l’oggetto Ordine completo di prodotti
            Ordine ordine = new Ordine(
                    orderId, idUtente, idPagamento, idIndirizzo,
                    statoOrdine, dataCreazione, dataAggiornamento,
                    importo, valuta, localeUtente,
                    listaProdotti
            );
            return Optional.of(ordine);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        // campi dell'ordine
        output.put("id", id);
        output.put("id_utente", idUtente);
        output.put("id_pagamento", idPagamento);
        output.put("id_indirizzo", idIndirizzo);
        output.put("stato_ordine", statoOrdine);
        output.put("data_creazione_ordine", DateManagement.printJSONCalendar(dataCreazioneOrdine));
        output.put("data_aggiornamento_stato_ordine", DateManagement.printJSONCalendar(dataAggiornamentoStatoOrdine));
        output.put("importo", importo);
        output.put("valuta", valuta);
        output.put("locale_utente", localeUtente);

        // array prodotti
        org.json.JSONArray prodottiArray = new org.json.JSONArray();
        for (ProdottiFull p : prodotti) {
            // p.toString() restituisce già JSON indentato, quindi lo ricostruiamo in JSONObject
            prodottiArray.put(new org.json.JSONObject(p.toString()));
        }
        output.put("prodotti", prodottiArray);

        return output.toString(4);
    }

}
