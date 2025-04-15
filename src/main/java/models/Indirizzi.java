package models;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Indirizzi implements Oggetti<Indirizzi> {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String citta;
    private final String stato;
    private final String cap;
    private final String indirizzo;
    private final String email;
    private final String numeroTelefono;


    public Indirizzi(int id,String nome,String cognome, String citta, String stato, String cap, String indirizzo,String email, String numeroTelefono) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.citta = citta;
        this.stato = stato;
        this.cap = cap;
        this.indirizzo = indirizzo;
        this.email = email;
        this.numeroTelefono = numeroTelefono;
    }
    public Indirizzi(){
        this(0,"","","","","","","","");
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCognome() { return cognome; }
    public String getCitta() { return citta; }
    public String getStato() { return stato; }
    public String getCap() { return cap; }
    public String getIndirizzo() { return indirizzo; }
    public String getEmail() { return email; }
    public String getNumeroTelefono() { return numeroTelefono; }


    @Override
    public Optional<Indirizzi> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String citta = rs.getString("citta");
            String stato = rs.getString("stato");
            String cap = rs.getString("cap");
            String indirizzo = rs.getString("indirizzo");
            String email = rs.getString("email");
            String numeroTelefono = rs.getString("numero_telefono");
            return Optional.of(new Indirizzi(id1,nome,cognome,citta,stato,cap,indirizzo,email,numeroTelefono));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("nome",nome);
        output.put("cognome",cognome);
        output.put("citta",citta);
        output.put("stato",stato);
        output.put("cap",cap);
        output.put("indirizzo",indirizzo);
        output.put("email",email);
        output.put("numero_telefono",numeroTelefono);
        return output.toString(4);
    }
}
