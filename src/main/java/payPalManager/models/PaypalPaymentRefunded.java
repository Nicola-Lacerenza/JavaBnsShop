package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import utility.DateManagement;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class PaypalPaymentRefunded implements Cloneable, Serializable,PaypalModels<PaypalPaymentRefunded> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int id;
    private final int idOrdine;
    private final int idUtente;
    private final Calendar data;
    private final double importo;
    private final String refundId;
    private final String valuta;
    private final String statoReso;

    public PaypalPaymentRefunded() {
        this(0,0,0,new GregorianCalendar(),0,"","","");
    }

    public PaypalPaymentRefunded(int id, int idOrdine, int idUtente, Calendar data, double importo, String refundId,String valuta,String statoReso) {
        this.id = id;
        this.idOrdine = idOrdine;
        this.idUtente = idUtente;
        this.data = data;
        this.importo = importo;
        this.refundId = refundId;
        this.valuta = valuta;
        this.statoReso = statoReso;
    }

    private PaypalPaymentRefunded(PaypalPaymentRefunded original) {
        this.id=original.id;
        this.idOrdine=original.idOrdine;
        this.idUtente=original.idUtente;
        this.data=original.data;
        this.importo=original.importo;
        this.refundId=original.refundId;
        this.valuta=original.valuta;
        this.statoReso=original.statoReso;
    }


    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalPaymentRefunded(this);
    }

    @Override
    public PaypalPaymentRefunded parseObjectFromJSON(JSONObject json) throws ParserException {
        return new PaypalPaymentRefunded();
    }

    @Override
    public PaypalPaymentRefunded copy() {
        return new PaypalPaymentRefunded(this);
    }

    @Override
    public JSONObject printJSONObject() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("id_ordine",idOrdine);
        output.put("id_utente",idUtente);
        output.put("data", DateManagement.printJSONCalendar(data));
        output.put("importo",importo);
        output.put("refund_id",refundId);
        output.put("valuta",valuta);
        output.put("stato_reso",statoReso);
        return output;
    }

    @Override
    public Optional<PaypalPaymentRefunded> convertDBToJava(ResultSet rs) throws SQLException {
        int id1 = rs.getInt("id");
        int idOrdine1 = rs.getInt("id_ordine");
        int idUtente1 = rs.getInt("id_utente");
        Timestamp data1 = rs.getTimestamp("data");
        double importo1 = rs.getDouble("importo");
        String refundId1 = rs.getString("refund_id");
        String valuta1 = rs.getString("valuta");
        String statoReso1 = rs.getString("stato_reso");
        PaypalPaymentRefunded refund = new PaypalPaymentRefunded(id1,idOrdine1,idUtente1, DateManagement.fromDatabaseToCalendar(data1),importo1,refundId1,valuta1,statoReso1);
        return Optional.of(refund);
    }
}