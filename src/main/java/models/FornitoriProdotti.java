package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class FornitoriProdotti implements Oggetti<FornitoriProdotti> {
    private final int idFornitore;
    private final int idProdotti;
    private final Calendar data;
    private final float importo;
    private final String descrizione;

    public FornitoriProdotti(int idFornitore, int idProdotti, Calendar data,float importo, String descrizione) {
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

    public float getImporto() {
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
    public Optional<FornitoriProdotti> convertDBToJava(ResultSet rs) {
        try{
            int idFornitore = rs.getInt("id_fornitore");
            int idProdotti = rs.getInt("id_prodotti");
            Calendar data = new GregorianCalendar();
            data.setTime(rs.getDate("data"));
            float importo = rs.getFloat("importo");
            String descrizione = rs.getString("descrizione");
            return Optional.of(new FornitoriProdotti(idFornitore,idProdotti,data,importo,descrizione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
