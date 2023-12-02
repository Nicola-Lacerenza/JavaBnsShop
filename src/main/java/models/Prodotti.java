package models;

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
}
