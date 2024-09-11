package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class CodiceSconto implements Oggetti<CodiceSconto> {

    private int id;
    private int idProdotti;
    private int idOrdine;
    private String codice;
    private double sconto;

    public CodiceSconto(int id, int idProdotti, int idOrdine, String codice, double sconto) {
        this.id = id;
        this.idProdotti = idProdotti;
        this.idOrdine = idOrdine;
        this.codice = codice;
        this.sconto = sconto;
    }

    public CodiceSconto(){
        this(0,0,0,"",0);
    }

    public int getId() {
        return id;
    }

    public int getIdProdotti() {
        return idProdotti;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public String getCodice() {
        return codice;
    }

    public double getSconto() {
        return sconto;
    }

    @Override
    public CodiceSconto createObject() {
        return new CodiceSconto();
    }

    @Override
    public CodiceSconto convertDBToJava(ResultSet rs) {
        try{
            int id1= rs.getInt("id");
            int idProdotti = rs.getInt("idProdotti");
            int idOrdine = rs.getInt("idOrdine");
            String codice = rs.getString("codice");
            double sconto = rs.getDouble("sconto");
            return new CodiceSconto(id1,idProdotti,idOrdine,codice,sconto);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
