package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DettagliOrdine implements Oggetti<DettagliOrdine> {
    private final int id;
    private final int idProdotti;
    private final int idOrdine;
    private final int quantita;
    private final int codiceSconto;
    private final String spedizione;

    public DettagliOrdine(int id,int idProdotti, int idOrdine, int quantita, int codiceSconto, String spedizione) {
        this.id = id;
        this.idProdotti = idProdotti;
        this.idOrdine = idOrdine;
        this.quantita = quantita;
        this.codiceSconto = codiceSconto;
        this.spedizione = spedizione;
    }

    public DettagliOrdine(){
        this(0,0,0,0,0,"");
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
    public Optional<DettagliOrdine> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idProdotti = rs.getInt("id_prodotti");
            int idOrdine = rs.getInt("id_ordine");
            int quantita = rs.getInt("quantita");
            int codiceSconto = rs.getInt("codice_sconto");
            String spedizione = rs.getString("spedizione");
            return Optional.of(new DettagliOrdine(id1,idProdotti,idOrdine,quantita,codiceSconto,spedizione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_prodotti",idProdotti);
        output.put("id_ordine",idOrdine);
        output.put("quantita",quantita);
        output.put("codice_sconto",codiceSconto);
        output.put("spedizione",spedizione);
        return output.toString(4);
    }
}
