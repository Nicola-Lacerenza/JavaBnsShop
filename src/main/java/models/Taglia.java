package models;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        return new Taglia();
    }

    @Override
    public Taglia convertDBToJava(ResultSet rs) {
        try{
            int idTaglia = rs.getInt("idTaglia");
            String taglia = rs.getString("taglia");
            return new Taglia(idTaglia, taglia);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
