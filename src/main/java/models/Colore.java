package models;

public class Colore {

    private int idColore;

    private String nome;

    private String rgb;

    private String hex;

    public Colore(int idColore, String nome, String rgb, String hex) {
        this.idColore = idColore;
        this.nome = nome;
        this.rgb = rgb;
        this.hex = hex;
    }

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
