package models;

public class Taglia {

    private int idTaglia;

    private String taglia;

    public Taglia(int idTaglia, String taglia) {
        this.idTaglia = idTaglia;
        this.taglia = taglia;
    }

    public String getTaglia() {
        return taglia;
    }

    public int getIdTaglia() {
        return idTaglia;
    }
}
