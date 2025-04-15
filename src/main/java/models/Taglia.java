package models;

import org.json.JSONObject;
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
    public Optional<Taglia> convertDBToJava(ResultSet rs) {
        try{
            int idTaglia = rs.getInt("id");
            String tagliaEu = rs.getString("taglia_Eu");
            String tagliaUk = rs.getString("taglia_Uk");
            String tagliaUs = rs.getString("taglia_Us");
            return Optional.of(new Taglia(idTaglia, tagliaEu,tagliaUk,tagliaUs));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("taglia_Eu",tagliaEu);
        output.put("taglia_Uk",tagliaUk);
        output.put("taglia_Us",tagliaUs);
        return output.toString(4);
    }
}
