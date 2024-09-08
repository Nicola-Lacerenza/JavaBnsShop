package models;

import java.sql.ResultSet;
import java.sql.Statement;

public class Taglia implements Oggetti<Taglia>{

    private int idTaglia;

    private String taglia;

    public Taglia(int idTaglia, String taglia) {
        this.idTaglia = idTaglia;
        this.taglia = taglia;
    }

    public Taglia(){
        this(0,"");
    }

    public String getTaglia() {
        return taglia;
    }

    public int getIdTaglia() {
        return idTaglia;
    }

    @Override
    public Taglia createObject() {
        return null;
    }

    @Override
    public Taglia convertDBToJava(ResultSet rs) {
        return null;
    }
}
