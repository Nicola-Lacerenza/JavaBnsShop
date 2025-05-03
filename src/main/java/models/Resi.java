package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Optional;

public class Resi implements Oggetti<Resi>{
    private final int id;
    private final int idUtente;
    private final int idOrdine;
    private final Calendar dataRichiesta;
    private final String motivo;
    private final Calendar DataRimborso;
    private final double importo;
    private final String refundId;
    private final String valuta;
    private final String statoReso;

    public Resi(int id, int idUtente, int idOrdine, Calendar dataRichiesta, String motivo, Calendar dataRimborso, double importo, String refundId, String valuta, String statoReso) {
        this.id = id;
        this.idUtente = idUtente;
        this.idOrdine = idOrdine;
        this.dataRichiesta = dataRichiesta;
        this.motivo = motivo;
        this.DataRimborso = dataRimborso;
        this.importo = importo;
        this.refundId = refundId;
        this.valuta = valuta;
        this.statoReso = statoReso;
    }

    @Override
    public Optional<Resi> convertDBToJava(ResultSet rs) throws SQLException {
        return Optional.empty();
    }
}
