package models;

import java.util.Calendar;

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
}
