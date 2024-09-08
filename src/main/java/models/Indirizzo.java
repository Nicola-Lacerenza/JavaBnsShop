package models;

import java.sql.ResultSet;
import java.sql.Statement;

public class Indirizzo implements Oggetti<Indirizzo> {

    private int id;
    private String citta;
    private String stato;
    private String cap;
    private String indirizzo;
    private int idCustomer;

    public Indirizzo(int id, String citta, String stato, String cap, String indirizzo, int idCustomer) {
        this.id = id;
        this.citta = citta;
        this.stato = stato;
        this.cap = cap;
        this.indirizzo = indirizzo;
        this.idCustomer = idCustomer;
    }
    public Indirizzo(){
        this(0,"","","","",0);
    }

    public int getId() {
        return id;
    }

    public String getCitta() {
        return citta;
    }

    public String getStato() {
        return stato;
    }

    public String getCap() {
        return cap;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    @Override
    public Indirizzo createObject() {
        return null;
    }

    @Override
    public Indirizzo convertDBToJava(ResultSet rs) {
        return null;
    }
}
