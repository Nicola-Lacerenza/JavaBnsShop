package models;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Modello implements Oggetti<Modello> {
    private final int id;
    private final int idBrand;
    private final int idCategoria;
    private final String nome;
    private final String descrizione;

    public Modello(int id, int idBrand, int idCategoria, String nome, String descrizione) {
        this.id = id;
        this.idBrand = idBrand;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Modello(){
        this(0,0,0,"","");
    }

    public int getId() {
        return id;
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
    public Optional<Modello> convertDBToJava(ResultSet rs) {
        try{
            int idModello = rs.getInt("id");
            int idBrand = rs.getInt("id_brand");
            int idCategoria = rs.getInt("id_categoria");
            String nome = rs.getString("nome");
            String descrizione = rs.getString("descrizione");
            return Optional.of(new Modello(idModello, idBrand, idCategoria, nome, descrizione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_brand",idBrand);
        output.put("id_categoria",idCategoria);
        output.put("nome",nome);
        output.put("descrizione",descrizione);
        return output.toString(4);
    }
}
