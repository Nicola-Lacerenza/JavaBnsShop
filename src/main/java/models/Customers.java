package models;

import java.sql.ResultSet;
import java.sql.Statement;

public class Customers implements Oggetti<Customers> {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private int telefono;

    public Customers(int id, String nome, String cognome, String email, int telefono) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }
    public Customers(){
        this(0,"","","",0);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    @Override
    public Customers createObject() {
        return null;
    }

    @Override
    public Customers convertDBToJava(ResultSet rs) {
        return null;
    }
}
