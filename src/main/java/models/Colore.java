package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Colore implements Oggetti<Colore> {

    private int idColore;

    private String nome;

    private String rgb;

    private String hex;

    //costruttore di classe
    public Colore(int idColore, String nome, String rgb, String hex) {
        this.idColore = idColore;
        this.nome = nome;
        this.rgb = rgb;
        this.hex = hex;
    }

    public Colore(){
        this(0,"","","");
    }
    //interfacciare il database conm l'applicazione
    public int getIdColore() {
        return idColore;
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
    public Colore convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("idColore");
            String nome = rs.getString("nome");
            String rgb = rs.getString("rgb");
            String hex = rs.getString("hex");
            return new Colore(id1,nome,rgb,hex);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
