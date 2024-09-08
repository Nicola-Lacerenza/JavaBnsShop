package models;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class Categoria implements Oggetti<Categoria> {
    private int idCategoria;

    private String nomeCategoria;

    public Categoria(int idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    public Categoria(){
        this(0,"");
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @Override
    public Categoria createObject() {
        return null;
    }

    @Override
    public Categoria convertDBToJava(ResultSet rs) {
        return null;
    }
}

