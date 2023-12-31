package models;

public class Colore implements Oggetti<Colore> {

    private int idColore;

    private String nome;

    private String rgb;

    private String hex;

    //costruttore di classe
    public Colore(int idColore, String nome, String rgb, String hex) {
        this.idColore = idColore;
        this.nome = nome;
        this.rgb = rgb;
        this.hex = hex;
    }

    //interfacciare il database conm l'applicazione
    public int getIdColore() {
        return idColore;
    }

    public String getNome() {
        return nome;
    }

    public String getRgb() {
        return rgb;
    }

    public String getHex() {
        return hex;
    }
}
