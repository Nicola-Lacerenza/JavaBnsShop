package models;

import org.json.JSONArray;
import org.json.JSONObject;
import utility.DateManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

public class Resi implements Oggetti<Resi>{
    private final int id;
    private final int idUtente;
    private final int idOrdine;
    private final Calendar dataRichiesta;
    private final String motivo;
    private final Calendar dataRimborso;
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
        this.dataRimborso = dataRimborso;
        this.importo = importo;
        this.refundId = refundId;
        this.valuta = valuta;
        this.statoReso = statoReso;
    }

    public Resi() {
        this(0,0,0,new GregorianCalendar(),"",new GregorianCalendar(),0.0,"","","");
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_utente",idUtente);
        output.put("id_ordine",idOrdine);
        output.put("data_richiesta",DateManagement.printJSONCalendar(dataRichiesta));
        output.put("motivo",motivo);
        output.put("data_rimborso",DateManagement.printJSONCalendar(dataRimborso));
        output.put("importo",importo);
        output.put("refund_id",refundId);
        output.put("valuta",valuta);
        output.put("stato_reso",statoReso);
        return output.toString(4);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resi resi = (Resi) o;
        return id == resi.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Optional<Resi> convertDBToJava(ResultSet rs) throws SQLException {
        int id1 = rs.getInt("id");
        int idUtente1 = rs.getInt("id_utente");
        int idOrdine1 = rs.getInt("id_ordine");
        Timestamp dataRichiesta1 = rs.getTimestamp("data_richiesta");
        Calendar dataRichiesta2 = DateManagement.fromDatabaseToCalendar(dataRichiesta1);
        String motivo1 = rs.getString("motivo");
        Timestamp dataRimborso1 = rs.getTimestamp("data_rimborso");
        Calendar dataRimborso2;
        if (dataRimborso1 != null){
            dataRimborso2 = DateManagement.fromDatabaseToCalendar(dataRimborso1);
        }else{
            dataRimborso2 = new GregorianCalendar();
        }
        double importo1 = rs.getDouble("importo");
        String refundId1 = rs.getString("refund_id");
        String refundId2;
        if (refundId1 != null){
            refundId2= refundId1;
        }else{
            refundId2 = "";
        }
        String valuta1 = rs.getString("valuta");
        String statoReso1 = rs.getString("stato_reso");
        Resi reso = new Resi(id1,idUtente1,idOrdine1,dataRichiesta2,motivo1,dataRimborso2,importo1,refundId2,valuta1,statoReso1);
        return Optional.of(reso);
    }
}
