package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DettagliOrdine implements Oggetti<DettagliOrdine> {
    private final int idProdotti;
    private final int idOrdine;
    private final int quantita;
    private final int codiceSconto;
    private final String spedizione;

    public DettagliOrdine(int idProdotti, int idOrdine, int quantita, int codiceSconto, String spedizione) {
        this.idProdotti = idProdotti;
        this.idOrdine = idOrdine;
        this.quantita = quantita;
        this.codiceSconto = codiceSconto;
        this.spedizione = spedizione;
    }

    public DettagliOrdine(){
        this(0,0,0,0,"");
    }

    public int getIdProdotti() {
        return idProdotti;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public int getQuantita() {
        return quantita;
    }

    public int getCodiceSconto() {
        return codiceSconto;
    }

    public String getSpedizione() {
        return spedizione;
    }

    @Override
    public DettagliOrdine createObject() {
        return new DettagliOrdine();
    }

    @Override
    public Optional<DettagliOrdine> convertDBToJava(ResultSet rs) {
        try{
            int idProdotti = rs.getInt("id_prodotti");
            int idOrdine = rs.getInt("id_ordine");
            int quantita = rs.getInt("quantita");
            int codiceSconto = rs.getInt("codice_sconto");
            String spedizione = rs.getString("spedizione");
            return Optional.of(new DettagliOrdine(idProdotti,idOrdine,quantita,codiceSconto,spedizione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
