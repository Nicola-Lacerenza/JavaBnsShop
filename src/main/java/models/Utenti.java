package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class Utenti implements Oggetti<Utenti> {
    private final int id;
    private final String nome;
    private final String cognome;
    private final Calendar dataNascita;
    private final String luogoNascita;
    private final String sesso;
    private final String email;
    private final String telefono;
    private final String password;
    private final String ruolo;

    public Utenti(int id, String nome, String cognome, Calendar dataNascita, String luogoNascita, String sesso, String email, String telefono, String password, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.sesso = sesso;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.ruolo = ruolo;
    }

    public Utenti(){
        this(0,"","",Calendar.getInstance(),"","","","","","");
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Calendar getDataNascita() {
        return dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public String getSesso() {
        return sesso;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getPassword() {
        return password;
    }

    public String getRuolo() { return ruolo;}

    @Override
    public Optional<Utenti> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome1 = rs.getString("nome");
            String cognome1 = rs.getString("cognome");
            Calendar dataNascita1 = new GregorianCalendar();
            dataNascita1.setTime(rs.getDate("data_nascita"));
            String luogoNascita1 = rs.getString("luogo_nascita");
            String sesso1 = rs.getString("sesso");
            String email1 = rs.getString("email");
            String telefono1 = rs.getString("telefono");
            String password1 = rs.getString("password");
            String ruolo1 = rs.getString("ruolo");
            return Optional.of(new Utenti(id1,nome1,cognome1,dataNascita1,luogoNascita1,sesso1,email1,telefono1,password1,ruolo1));
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
        output.put("data_nascita",dataNascita);
        output.put("luogo_nascita",luogoNascita);
        output.put("sesso",sesso);
        output.put("email",email);
        output.put("telefono",telefono);
        output.put("password",password);
        output.put("ruolo",ruolo);
        return output.toString(4);
    }
}
