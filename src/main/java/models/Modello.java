package models;

import java.sql.ResultSet;
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
        return null;
    }

    @Override
    public Modello convertDBToJava(ResultSet rs) {
        return null;
    }
}
