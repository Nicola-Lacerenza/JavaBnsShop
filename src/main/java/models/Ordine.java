package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class Ordine implements Oggetti<Ordine>{
    private final int id;
    private final int idCustomers;
    private final int idPagamento;
    private final int idIndirizzo;
    private final String statoOrdine;
    private final Calendar dataOrdine;

    public Ordine(int id, int idCustomers, int idPagamento, int idIndirizzo, String statoOrdine, Calendar dataOrdine) {
        this.id = id;
        this.idCustomers = idCustomers;
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

    public int getIdCustomers() {
        return idCustomers;
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
    public Ordine createObject() {
        return new Ordine();
    }

    @Override
    public Optional<Ordine> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idCustomers = rs.getInt("id_customers");
            int idPagamento = rs.getInt("id_pagamento");
            int idIndirizzo = rs.getInt("id_indirizzo");
            String statoOrdine = rs.getString("stato_ordine");
            Calendar dataOrdine = new GregorianCalendar();
            dataOrdine.setTime(rs.getDate("data_ordine"));
            return Optional.of(new Ordine(id1,idCustomers,idPagamento,idIndirizzo,statoOrdine,dataOrdine));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
