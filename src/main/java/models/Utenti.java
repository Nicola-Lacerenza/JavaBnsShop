package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utenti implements Oggetti<Utenti> {

    private int idUtente;

    private String nome;

    private String cognome;

    private Calendar dataNascita;
    private String luogoNascita;
    private String sesso;
    private String email;
    private String telefono;
    private String password;

    public Utenti(int idUtente, String nome, String cognome, Calendar dataNascita, String luogoNascita, String sesso, String email, String telefono, String password) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.sesso = sesso;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
    }

    public Utenti(){
        this(0,"","",Calendar.getInstance(),"","","","","");
    }

    public int getIdUtente() {
        return idUtente;
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

    @Override
    public Utenti createObject() {
        return new Utenti();
    }

    @Override
    public Utenti convertDBToJava(ResultSet rs) {
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
            return new Utenti(id1,nome1,cognome1,dataNascita1,luogoNascita1,sesso1,email1,telefono1,password1);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}