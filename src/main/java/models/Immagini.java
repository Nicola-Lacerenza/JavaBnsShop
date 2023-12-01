package models;

public class Immagini {

    private int idImmagini;

    private int idProdotti;

    private String url;

    public Immagini(int idImmagini, int idProdotti, String url) {
        this.idImmagini = idImmagini;
        this.idProdotti = idProdotti;
        this.url = url;
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
}
