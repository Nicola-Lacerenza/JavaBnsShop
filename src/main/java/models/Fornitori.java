package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Fornitori implements Oggetti<Fornitori>{
    private final int id;
    private final String nome;
    private final String cognome;

    public Fornitori(int id,String nome, String cognome) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
    }
    public Fornitori(){
        this(0,"","");
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

    @Override
    public Fornitori createObject() {
        return new Fornitori();
    }

    @Override
    public Optional<Fornitori> convertDBToJava(ResultSet rs) {
        try{
            int idFornitore = rs.getInt("id");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            return Optional.of(new Fornitori(idFornitore,nome,cognome));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
