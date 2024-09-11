package models;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        return new Categoria();
    }

    @Override
    public Categoria convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("idCategoria");
            String nomeCategoria = rs.getString("nomeCategoria");
            return new Categoria(id1,nomeCategoria);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

