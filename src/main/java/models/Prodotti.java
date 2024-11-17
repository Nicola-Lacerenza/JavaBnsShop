package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Prodotti implements Oggetti<Prodotti>{
    private final int id;
    private final int idModello;
    private final double prezzo;
    private final int statoPubblicazione;

    public Prodotti(int id, int idModello, double prezzo, int statoPubblicazione) {
        this.id = id;
        this.idModello = idModello;
        this.prezzo = prezzo;
        this.statoPubblicazione = statoPubblicazione;
    }
    public Prodotti(){
        this(0,0,0,0);
    }

    public int getId() {
        return id;
    }

    public int getIdModello() {
        return idModello;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public int getStatoPubblicazione() {
        return statoPubblicazione;
    }

    @Override
    public Prodotti createObject() {
        return new Prodotti();
    }

    @Override
    public Optional<Prodotti> convertDBToJava(ResultSet rs) {
        try{
            int idProdotto = rs.getInt("id");
            int idModello = rs.getInt("id_modello");
            double prezzo = rs.getDouble("prezzo");
            int statoPubblicazione = rs.getInt("stato_pubblicazione");
            return Optional.of(new Prodotti(idProdotto,idModello, prezzo, statoPubblicazione));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_modello",idModello);
        output.put("prezzo",prezzo);
        output.put("stato_pubblicazione",statoPubblicazione);
        return output.toString(4);
    }
}
