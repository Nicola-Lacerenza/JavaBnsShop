package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
        return new Indirizzo();
    }

    @Override
    public Indirizzo convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String citta = rs.getString("citta");
            String stato = rs.getString("stato");
            String cap = rs.getString("cap");
            String indirizzo = rs.getString("indirizzo");
            int idCustomer = rs.getInt("idCustomer");
            return new Indirizzo(id1,citta,stato,cap,indirizzo,idCustomer);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
