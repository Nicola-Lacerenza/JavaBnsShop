package models;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class Brand implements Oggetti<Brand> {

    private int idBrand;

    private String nome;

    private String descrizione;

    public Brand(int idBrand, String nome, String descrizione) {
        this.idBrand = idBrand;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Brand(){
        this(0,"","");
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

    @Override
    public Brand createObject() {
        return null;
    }

    @Override
    public Brand convertDBToJava(ResultSet rs) {
        return null;
    }
}
