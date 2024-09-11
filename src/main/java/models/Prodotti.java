package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class Prodotti implements Oggetti<Prodotti>{
    private int idProdotto;
    private int idModello;
    private int idTaglia;
    private double prezzo;
    private int quantita;
    private int statoPubblicazione;

    public Prodotti(int idProdotto, int idModello, int idTaglia, double prezzo, int quantita, int statoPubblicazione) {
        this.idProdotto = idProdotto;
        this.idModello = idModello;
        this.idTaglia = idTaglia;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.statoPubblicazione = statoPubblicazione;
    }
    public Prodotti(){
        this(0,0,0,0,0,0);
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public int getIdModello() {
        return idModello;
    }

    public int getIdTaglia() {
        return idTaglia;
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
    public Prodotti convertDBToJava(ResultSet rs) {
        try{
            int idProdotto = rs.getInt("idProdotto");
            int idModello = rs.getInt("idModello");
            int idTaglia = rs.getInt("idTaglia");
            double prezzo = rs.getDouble("prezzo");
            int quantita = rs.getInt("quantita");
            int statoPubblicazione = rs.getInt("statoPubblicazione");
            return new Prodotti(idProdotto,idModello,idTaglia, prezzo, quantita, statoPubblicazione);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
