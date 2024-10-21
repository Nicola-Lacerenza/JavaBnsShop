package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Prodotti implements Oggetti<Prodotti>{
    private final int id;
    private final int idModello;
    private final int idTaglia;
    private final int idImmagini;
    private final double prezzo;
    private final int quantita;
    private final int statoPubblicazione;

    public Prodotti(int id, int idModello, int idTaglia, int idImmagini, double prezzo, int quantita, int statoPubblicazione) {
        this.id = id;
        this.idModello = idModello;
        this.idTaglia = idTaglia;
        this.idImmagini = idImmagini;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.statoPubblicazione = statoPubblicazione;
    }
    public Prodotti(){
        this(0,0,0,0,0,0,0);
    }

    public int getId() {
        return id;
    }

    public int getIdModello() {
        return idModello;
    }

    public int getIdTaglia() {
        return idTaglia;
    }
    public int getIdImmagini() {
        return idImmagini;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public int getQuantita() {
        return quantita;
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
            int idTaglia = rs.getInt("id_taglia");
            int idImmagini = rs.getInt("id_immagini");
            double prezzo = rs.getDouble("prezzo");
            int quantita = rs.getInt("quantita");
            int statoPubblicazione = rs.getInt("stato_pubblicazione");
            return Optional.of(new Prodotti(idProdotto,idModello,idTaglia,idImmagini, prezzo, quantita, statoPubblicazione));
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
        output.put("id_taglia",idTaglia);
        output.put("id_immagini",idImmagini);
        output.put("prezzo",prezzo);
        output.put("quantita",quantita);
        output.put("stato_pubblicazione",statoPubblicazione);
        return output.toString(4);
    }
}
