package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Categoria implements Oggetti<Categoria> {
    private final int id;
    private final String nomeCategoria;
    private final String target;

    public Categoria(int id, String nomeCategoria,String target) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.target = target;
    }

    public Categoria(){
        this(0,"","");
    }

    public int getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }
    public String getTarget() {
        return target;
    }

    @Override
    public Optional<Categoria> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nomeCategoria = rs.getString("nome_categoria");
            String target = rs.getString("target");
            return Optional.of(new Categoria(id1,nomeCategoria,target));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("nome_categoria",nomeCategoria);
        output.put("target",target);
        return output.toString(4);
    }
}

