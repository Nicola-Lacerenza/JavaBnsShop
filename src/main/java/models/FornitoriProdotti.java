package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FornitoriProdotti implements Oggetti<FornitoriProdotti> {

    private int idFornitore;

    private int idProdotti;

    private Calendar data;

    private int importo;

    private String descrizione;

    public FornitoriProdotti(int idFornitore, int idProdotti, Calendar data, int importo, String descrizione) {
        this.idFornitore = idFornitore;
        this.idProdotti = idProdotti;
        this.data = data;
        this.importo = importo;
        this.descrizione = descrizione;
    }
    public FornitoriProdotti(){
        this(0,0,Calendar.getInstance(),0,"");
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

    public int getImporto() {
        return importo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public FornitoriProdotti createObject() {
        return new FornitoriProdotti();
    }

    @Override
    public FornitoriProdotti convertDBToJava(ResultSet rs) {
        try{
            int idFornitore = rs.getInt("idFornitore");
            int idProdotti = rs.getInt("idProdotti");
            Calendar data = new GregorianCalendar();
            data.setTime(rs.getDate("data"));
            int importo = rs.getInt("importo");
            String descrizione = rs.getString("descrizione");
            return new FornitoriProdotti(idFornitore,idProdotti,data,importo,descrizione);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
