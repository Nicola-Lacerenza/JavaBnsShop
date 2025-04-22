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
            // ——— 1) Campi dell’ordine —————————————————————
            int orderId       = rs.getInt("id");
            int idUtente      = rs.getInt("id_utente");
            int idPagamento   = rs.getInt("id_pagamento");
            int idIndirizzo   = rs.getInt("id_indirizzo");
            String stato      = rs.getString("stato_ordine");
            Calendar crea     = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_creazione_ordine"));
            Calendar aggiorna = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_aggiornamento_stato_ordine"));
            double importo    = rs.getDouble("importo");
            String valuta     = rs.getString("valuta");
            String locale     = rs.getString("locale_utente");

            // ——— 2) Map per raggruppare i ProdottiFull by id_prodotto ———

            // ——— 3) Ciclo su tutte le righe dell’ordine —————————
                int idProdotto = rs.getInt("id_prodotto");

                ProdottiFull p;
                    int modelloId        = rs.getInt("id_modello");
                    String nomeModello   = rs.getString("nome_modello");
                    String descModello   = rs.getString("descrizione_modello");
                    int catId            = rs.getInt("id_categoria");
                    String nomeCat       = rs.getString("nome_categoria");
                    String target        = rs.getString("target");
                    int brandId          = rs.getInt("id_brand");
                    String nomeBrand     = rs.getString("nome_brand");
                    String descBrand     = rs.getString("descrizione_brand");
                    int statoPub         = rs.getInt("stato_pubblicazione");
                    double prezzo        = rs.getDouble("prezzo");

                    // istanzio con liste vuote
                    p = new ProdottiFull(
                            idProdotto,
                            modelloId,
                            nomeModello,
                            descModello,
                            catId,
                            nomeCat,
                            target,
                            brandId,
                            nomeBrand,
                            descBrand,
                            statoPub,
                            prezzo,
                            new LinkedList<>(),
                            new LinkedList<>(),
                            new LinkedList<>()
                    );

                // ——— 3a) Estrai la singola “combinazione” e aggiungila alle liste ———
                String tagEu = rs.getString("taglia_Eu");
                String tagUk = rs.getString("taglia_Uk");
                String tagUs = rs.getString("taglia_Us");
                int quantita = rs.getInt("quantita");
                p.getTaglieProdotto().add(new ProdottiFull.ProdottiTaglieEstratte(
                        new Taglia(0, tagEu, tagUk, tagUs),
                        new TaglieProdotti(0, 0, idProdotto, quantita)
                ));

                String url    = rs.getString("url");
                if (url != null) p.getUrl().add(url);

                String colore = rs.getString("nome_colore");
                if (colore != null) p.getNomeColore().add(colore);

            // ——— 4) Costruisci l’Ordine completo ————————————————
            List<ProdottiFull> listaProdotti = new LinkedList<>();
            listaProdotti.add(p);
            Ordine ordine = new Ordine(
                    orderId, idUtente, idPagamento, idIndirizzo,
                    stato, crea, aggiorna,
                    importo, valuta, locale,
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Ordine)){
            return false;
        }
        Ordine ordine = (Ordine)(obj);
        return id == ordine.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
