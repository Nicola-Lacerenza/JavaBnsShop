package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class FornitoriProdotti implements Oggetti<FornitoriProdotti> {
    private final int id;
    private final int idFornitore;
    private final int idProdotti;
    private final Calendar data;
    private final float importo;
    private final String descrizione;

    public FornitoriProdotti(int id,int idFornitore, int idProdotti, Calendar data,float importo, String descrizione) {
        this.id = id;
        this.idFornitore = idFornitore;
        this.idProdotti = idProdotti;
        this.data = data;
        this.importo = importo;
        this.descrizione = descrizione;
    }
    public FornitoriProdotti(){
        this(0,0,0,Calendar.getInstance(),0,"");
    }

    public int getId() {
        return id;
    }
    public int getIdFornitore() {
        return idFornitore;
    }

    public int getIdProdotti() {
        return idProdotti;
    }

    public Calendar getData() {
        return data;
    }

    public float getImporto() {
        return importo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public Optional<FornitoriProdotti> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idFornitore = rs.getInt("id_fornitore");
            int idProdotti = rs.getInt("id_prodotti");
            Calendar data = new GregorianCalendar();
            data.setTime(rs.getDate("data"));
            float importo = rs.getFloat("importo");
            String descrizione = rs.getString("descrizione");
            return Optional.of(new FornitoriProdotti(id1,idFornitore,idProdotti,data,importo,descrizione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_fornitore",idFornitore);
        output.put("id_prodotti",idProdotti);
        output.put("data",data);
        output.put("importo",importo);
        output.put("descrizione",descrizione);
        return output.toString(4);
    }
}
