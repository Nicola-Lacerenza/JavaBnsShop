package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Taglia implements Oggetti<Taglia>{
    private final int id;
    private final String tagliaEu;
    private final String tagliaUk;
    private final String tagliaUs;

    public Taglia(int id, String tagliaEu,String tagliaUk,String tagliaUs) {
        this.id = id;
        this.tagliaEu = tagliaEu;
        this.tagliaUk = tagliaUk;
        this.tagliaUs = tagliaUs;
    }

    public Taglia(){
        this(0,"","","");
    }

    public String getTagliaEu() {
        return tagliaEu;
    }
    public String getTagliaUk() {
        return tagliaUk;
    }
    public String getTagliaUs() {
        return tagliaUs;
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
            String tagliaEu = rs.getString("tagliaEu");
            String tagliaUk = rs.getString("tagliaUk");
            String tagliaUs = rs.getString("tagliaUs");
            return Optional.of(new Taglia(idTaglia, tagliaEu,tagliaUk,tagliaUs));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
