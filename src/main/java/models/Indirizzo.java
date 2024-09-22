package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Indirizzo implements Oggetti<Indirizzo> {
    private final int id;
    private final String citta;
    private final String stato;
    private final String cap;
    private final String indirizzo;
    private final int idCustomer;

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
    public Optional<Indirizzo> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String citta = rs.getString("citta");
            String stato = rs.getString("stato");
            String cap = rs.getString("cap");
            String indirizzo = rs.getString("indirizzo");
            int idCustomer = rs.getInt("id_customers");
            return Optional.of(new Indirizzo(id1,citta,stato,cap,indirizzo,idCustomer));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("citta",citta);
        output.put("stato",stato);
        output.put("cap",cap);
        output.put("indirizzo",indirizzo);
        output.put("id_customers",idCustomer);
        return output.toString(4);
    }
}
