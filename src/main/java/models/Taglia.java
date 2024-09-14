package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Taglia implements Oggetti<Taglia>{
    private final int id;
    private final String taglia;

    public Taglia(int id, String taglia) {
        this.id = id;
        this.taglia = taglia;
    }

    public Taglia(){
        this(0,"");
    }

    public String getTaglia() {
        return taglia;
    }

    public int getId() {
        return id;
    }

    @Override
    public Taglia createObject() {
        return new Taglia();
    }

    @Override
    public Optional<Taglia> convertDBToJava(ResultSet rs) {
        try{
            int idTaglia = rs.getInt("id");
            String taglia = rs.getString("taglia");
            return Optional.of(new Taglia(idTaglia, taglia));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
