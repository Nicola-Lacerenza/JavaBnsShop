package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Colore implements Oggetti<Colore> {
    private final int id;
    private final String nome;


    //costruttore di classe
    public Colore(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Colore(){
        this(0,"");
    }
    //interfacciare il database conm l'applicazione
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public Optional<Colore> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome = rs.getString("nome");
            return Optional.of(new Colore(id1,nome));
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
        return output.toString(4);
    }
}
