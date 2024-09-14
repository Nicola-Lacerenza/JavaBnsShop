package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CodiceSconto implements Oggetti<CodiceSconto>{
    private final int id;
    private final int idProdotti;
    private final int idOrdine;
    private final String codice;
    private final double sconto;

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
    public Optional<CodiceSconto> convertDBToJava(ResultSet rs) {
        try{
            int id1= rs.getInt("id");
            int idProdotti = rs.getInt("id_prodotti");
            int idOrdine = rs.getInt("id_ordine");
            String codice = rs.getString("codice");
            double sconto = rs.getDouble("sconto");
            return Optional.of(new CodiceSconto(id1,idProdotti,idOrdine,codice,sconto));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
