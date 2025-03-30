package models;

import org.json.JSONObject;
import utility.DateManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
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

    public Ordine(int id, int idUtente, int idPagamento, int idIndirizzo, String statoOrdine, Calendar dataCreazioneOrdine,Calendar dataAggiornamentoStatoOrdine,double importo,String valuta,String localeUtente) {
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
    }

    public Ordine(){
        this(0,0,0,0,"",Calendar.getInstance(),Calendar.getInstance(),0.0,"","");
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

    @Override
    public Optional<Ordine> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idUtente = rs.getInt("id_utente");
            int idPagamento = rs.getInt("id_pagamento");
            int idIndirizzo = rs.getInt("id_indirizzo");
            String statoOrdine = rs.getString("stato_ordine");
            Calendar dataCreazioneOrdine = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_creazione_ordine"));
            Calendar dataAggiornamentoStatoOrdine = DateManagement.fromDatabaseToCalendar(rs.getTimestamp("data_aggiornamento_stato_ordine"));
            double importo = rs.getDouble("importo");
            String valuta = rs.getString("valuta");
            String localeUtente = rs.getString("locale_utente");
            return Optional.of(new Ordine(id1,idUtente,idPagamento,idIndirizzo,statoOrdine,dataCreazioneOrdine,dataAggiornamentoStatoOrdine,importo,valuta,localeUtente));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_utente",idUtente);
        output.put("id_pagamento",idPagamento);
        output.put("id_indirizzo",idIndirizzo);
        output.put("stato_ordine",statoOrdine);
        output.put("data_creazione_ordine",DateManagement.printJSONCalendar(dataCreazioneOrdine));
        output.put("data_aggiornamento_stato_ordine",DateManagement.printJSONCalendar(dataAggiornamentoStatoOrdine));
        output.put("importo",importo);
        output.put("valuta",valuta);
        output.put("locale_utente",localeUtente);
        return output.toString(4);
    }
}
