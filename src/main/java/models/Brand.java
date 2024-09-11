package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
        return new Brand();
    }

    @Override
    public Brand convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome = rs.getString("nome");
            String descrizione = rs.getString("descrizione");
            return new Brand(id1,nome,descrizione);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
