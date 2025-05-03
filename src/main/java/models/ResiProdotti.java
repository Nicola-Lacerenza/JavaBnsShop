package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ResiProdotti implements Oggetti<ResiProdotti>{

    private final int id;
    private final int idReso;
    private final int idProdotto;
    private final int idTaglia;
    private final int quantita;
    private final double prezzoUnitario;

    public ResiProdotti(int id, int idReso, int idProdotto,int idTaglia, int quantita, double prezzoUnitario) {
        this.id = id;
        this.idReso = idReso;
        this.idProdotto = idProdotto;
        this.idTaglia = idTaglia;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }

    @Override
    public Optional<ResiProdotti> convertDBToJava(ResultSet rs) throws SQLException {
        return Optional.empty();
    }
}
