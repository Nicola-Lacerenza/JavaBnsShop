package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Immagini implements Oggetti<Immagini>{
    private final int id;
    private final int idProdotti;
    private final String url;

    public Immagini(int id, int idProdotti, String url) {
        this.id = id;
        this.idProdotti = idProdotti;
        this.url = url;
    }
    public Immagini(){
        this(0,0,"");
    }
    public int getId() {
        return id;
    }

    public int getIdProdotti() {
        return idProdotti;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Immagini createObject() {
        return new Immagini();
    }

    @Override
    public Optional<Immagini> convertDBToJava(ResultSet rs) {
        try{
            int idImmagini = rs.getInt("id");
            int idProdotti = rs.getInt("id_prodotti");
            String url = rs.getString("url");
            return Optional.of(new Immagini(idImmagini,idProdotti,url));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
