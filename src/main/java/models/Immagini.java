package models;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Immagini implements Oggetti<Immagini>{
    private final int id;
    private final String url;

    public Immagini(int id, String url) {
        this.id = id;
        this.url = url;
    }
    public Immagini(){
        this(0,"");
    }
    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Optional<Immagini> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String url = rs.getString("url");
            return Optional.of(new Immagini(id1,url));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("url",url);
        return output.toString(4);
    }
}
