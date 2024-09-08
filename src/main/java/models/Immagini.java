package models;

import java.sql.ResultSet;
import java.sql.Statement;

public class Immagini implements Oggetti<Immagini>{

    private int idImmagini;

    private int idProdotti;

    private String url;

    public Immagini(int idImmagini, int idProdotti, String url) {
        this.idImmagini = idImmagini;
        this.idProdotti = idProdotti;
        this.url = url;
    }
    public Immagini(){
        this(0,0,"");
    }
    public int getIdImmagini() {
        return idImmagini;
    }

    public int getIdProdotti() {
        return idProdotti;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Immagini createObject() {
        return null;
    }

    @Override
    public Immagini convertDBToJava(ResultSet rs) {
        return null;
    }
}
