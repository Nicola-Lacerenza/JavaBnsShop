package models;

import java.sql.ResultSet;
import java.sql.Statement;

public class DettagliOrdine implements Oggetti<DettagliOrdine> {

    private int idProdotti;
    private int idOrdine;
    private int quantita;
    private int codiceSconto;
    private String spedizione;
    private String dettagliOrdine;

    public DettagliOrdine(int idProdotti, int idOrdine, int quantita, int codiceSconto, String spedizione, String dettagliOrdine) {
        this.idProdotti = idProdotti;
        this.idOrdine = idOrdine;
        this.quantita = quantita;
        this.codiceSconto = codiceSconto;
        this.spedizione = spedizione;
        this.dettagliOrdine = dettagliOrdine;
    }

    public DettagliOrdine(){
        this(0,0,0,0,"","");
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

    public String getDettagliOrdine() {
        return dettagliOrdine;
    }

    @Override
    public DettagliOrdine createObject() {
        return null;
    }

    @Override
    public DettagliOrdine convertDBToJava(ResultSet rs) {
        return null;
    }
}
