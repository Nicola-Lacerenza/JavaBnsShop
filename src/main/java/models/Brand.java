package models;

public class Brand {

    private int idBrand;

    private String nome;

    private String descrizione;

    public Brand(int idBrand, String nome, String descrizione) {
        this.idBrand = idBrand;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
