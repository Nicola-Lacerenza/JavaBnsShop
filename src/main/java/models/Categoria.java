package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Categoria implements Oggetti<Categoria> {
    private final int id;
    private final String nomeCategoria;

    public Categoria(int id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }

    public Categoria(){
        this(0,"");
    }

    public int getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @Override
    public Categoria createObject() {
        return new Categoria();
    }

    @Override
    public Optional<Categoria> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nomeCategoria = rs.getString("nome_categoria");
            return Optional.of(new Categoria(id1,nomeCategoria));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

