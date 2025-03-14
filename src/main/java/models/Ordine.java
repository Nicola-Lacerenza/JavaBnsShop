package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class Ordine implements Oggetti<Ordine>{
    private final int id;
    private final int idUtente;
    private final int idPagamento;
    private final int idIndirizzo;
    private final String statoOrdine;
    private final Calendar dataOrdine;

    public Ordine(int id, int idUtente, int idPagamento, int idIndirizzo, String statoOrdine, Calendar dataOrdine) {
        this.id = id;
        this.idUtente = idUtente;
        this.idPagamento = idPagamento;
        this.idIndirizzo = idIndirizzo;
        this.statoOrdine = statoOrdine;
        this.dataOrdine = dataOrdine;
    }

    public Ordine(){
        this(0,0,0,0,"",Calendar.getInstance());
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

    public Calendar getDataOrdine() {
        return dataOrdine;
    }

    @Override
    public Optional<Ordine> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idUtente = rs.getInt("id_utente");
            int idPagamento = rs.getInt("id_pagamento");
            int idIndirizzo = rs.getInt("id_indirizzo");
            String statoOrdine = rs.getString("stato_ordine");
            Calendar dataOrdine = new GregorianCalendar();
            dataOrdine.setTime(rs.getDate("data_ordine"));
            return Optional.of(new Ordine(id1,idUtente,idPagamento,idIndirizzo,statoOrdine,dataOrdine));
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
        output.put("data_ordine",dataOrdine);
        return output.toString(4);
    }
}
