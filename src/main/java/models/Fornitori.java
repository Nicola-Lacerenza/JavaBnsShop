package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class Fornitori implements Oggetti<Fornitori>{

    private int idFornitore;

    private String nome;

    private String cognome;

    public Fornitori(int idFornitore,String nome, String cognome) {
        this.idFornitore = idFornitore;
        this.nome = nome;
        this.cognome = cognome;
    }
    public Fornitori(){
        this(0,"","");
    }

    public int getIdFornitore() {
        return idFornitore;
    }

    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }

    @Override
    public Fornitori createObject() {
        return new Fornitori();
    }

    @Override
    public Fornitori convertDBToJava(ResultSet rs) {
        try{
            int idFornitore = rs.getInt("idFornitore");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            return new Fornitori(idFornitore,nome,cognome);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
