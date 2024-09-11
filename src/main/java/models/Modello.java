package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Modello implements Oggetti<Modello> {

    private int idModello;

    private int idBrand;

    private int idCategoria;

    private String nome;

    private String descrizione;

    public Modello(int idModello, int idBrand, int idCategoria, String nome, String descrizione) {
        this.idModello = idModello;
        this.idBrand = idBrand;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Modello(){
        this(0,0,0,"","");
    }

    public int getIdModello() {
        return idModello;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public Modello createObject() {
        return new Modello();
    }

    @Override
    public Modello convertDBToJava(ResultSet rs) {
        try{
            int idModello = rs.getInt("idModello");
            int idBrand = rs.getInt("idBrand");
            int idCategoria = rs.getInt("idCategoria");
            String nome = rs.getString("nome");
            String descrizione = rs.getString("descrizione");
            return new Modello(idModello, idBrand, idCategoria, nome, descrizione);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
