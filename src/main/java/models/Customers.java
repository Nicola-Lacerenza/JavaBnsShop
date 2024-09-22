package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Customers implements Oggetti<Customers> {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String email;
    private final int telefono;

    public Customers(int id, String nome, String cognome, String email, int telefono) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }
    public Customers(){
        this(0,"","","",0);
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

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    @Override
    public Customers createObject() {
        return new Customers();
    }

    @Override
    public Optional<Customers> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String email = rs.getString("email");
            int telefono = rs.getInt("telefono");
            return Optional.of(new Customers(id1,nome,cognome,email,telefono));
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
        output.put("email",email);
        output.put("telefono",telefono);
        return output.toString(4);
    }
}
