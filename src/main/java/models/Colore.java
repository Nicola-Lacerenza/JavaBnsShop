package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Colore implements Oggetti<Colore> {
    private final int id;
    private final String nome;
    private final String rgb;
    private final String hex;

    //costruttore di classe
    public Colore(int id, String nome, String rgb, String hex) {
        this.id = id;
        this.nome = nome;
        this.rgb = rgb;
        this.hex = hex;
    }

    public Colore(){
        this(0,"","","");
    }
    //interfacciare il database conm l'applicazione
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRgb() {
        return rgb;
    }

    public String getHex() {
        return hex;
    }

    @Override
    public Colore createObject() {
        return new Colore();
    }

    @Override
    public Optional<Colore> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome = rs.getString("nome");
            String rgb = rs.getString("rgb");
            String hex = rs.getString("hex");
            return Optional.of(new Colore(id1,nome,rgb,hex));
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
        output.put("rgb",rgb);
        output.put("hex",hex);
        return output.toString(4);
    }
}
