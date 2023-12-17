package models;

import java.util.Calendar;

public class Fornitori {

    private int idFornitore;

    private String nome;

    private String cognome;

    public Fornitori(int idFornitore,String nome, String cognome) {
        this.idFornitore = idFornitore;
        this.nome = nome;
        this.cognome = cognome;
    }

    public int getIdFornitore() {
        return idFornitore;
    }

    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
}
