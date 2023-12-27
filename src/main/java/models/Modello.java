package models;

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
}
