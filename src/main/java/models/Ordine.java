package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class Ordine implements Oggetti<Ordine>{

    private int id;
    private int idCustomers;
    private int idPagamento;
    private int idIndirizzo;
    private String statoOrdine;
    private Calendar dataOrdine;

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
        return null;
    }

    @Override
    public Ordine convertDBToJava(ResultSet rs) {
        return null;
    }
}
