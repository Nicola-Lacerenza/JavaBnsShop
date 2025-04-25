package payPalManager.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import payPalManager.models.Amount;
import payPalManager.models.PaypalPaymentRefunded;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.Serial;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RefundPaymentRequest extends PaypalAPIRequest<PaypalPaymentRefunded>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String refundLink;
    private final Amount amount;
    private final int idUtente;
    private final int idOrdine;

    private RefundPaymentRequest(Builder builder){
        super("",builder.accessToken);
        this.refundLink = builder.refundLink;
        this.amount = builder.amount;
        this.idUtente = builder.idUtente;
        this.idOrdine = builder.idOrdine;
    }

    @Override
    public String getEndpoint() {
        return refundLink;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Authorization","Bearer " + getAccessToken());
        headers.put("Prefer","return=representation");
        return headers;
    }

    @Override
    public String getBody() {
        JSONArray platformFees = new JSONArray();
        JSONObject platformFee = new JSONObject();
        platformFee.put("amount",amount.printJSONObject());
        platformFees.put(platformFee);
        JSONObject paymentInstruction = new JSONObject();
        paymentInstruction.put("platform_fees",platformFees);
        JSONObject data = new JSONObject();
        data.put("amount",amount.printJSONObject());
        data.put("payment_instruction",paymentInstruction);
        return data.toString(4);
    }

    @Override
    public Optional<PaypalPaymentRefunded> parseResponse(JSONObject jsonResponse) {
        //estrazione dei parametri dalla risposta json ricevuta dal server.
        String refundId = jsonResponse.getString("id");
        String status = jsonResponse.getString("status");
        JSONObject amountObject = jsonResponse.getJSONObject("amount");
        double amount = amountObject.getDouble("value");
        String currency = amountObject.getString("currency_code");

        //parametri per inserire il reso nel database.
        Map<Integer, QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_utente",idUtente,TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_ordine",idOrdine,TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("importo",amount, TipoVariabile.realNumber));
            fields1.put(3,new QueryFields<>("refund_id",refundId,TipoVariabile.string));
            fields1.put(4,new QueryFields<>("valuta",currency,TipoVariabile.string));
            fields1.put(5,new QueryFields<>("stato_reso",status,TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        //parametri che servono per l'aggiornamento della tabella ordine.
        Map<Integer, QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
        try{
            fields2.put(0,new QueryFields<>("stato_ordine","REFUNDED",TipoVariabile.string));
            fields2.put(1,new QueryFields<>("id_ordine_paypal",idOrdine,TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        AtomicInteger idReso = new AtomicInteger();
        boolean result = Database.executeTransaction(connection -> {
            idReso.set(Database.insertElement(connection,"resi", fields1));
            if(idReso.get() <= 0){
                return false;
            }
            String query = "UPDATE ordine SET stato_ordine = ?,data_aggiornamento_stato_ordine = NOW() WHERE id_ordine_paypal = ?";
            return Database.executeGenericUpdate(connection,query,fields2);
        });

        if(result){
            PaypalPaymentRefunded paymentRefunded = new PaypalPaymentRefunded(idReso.get(),idOrdine,idUtente,new GregorianCalendar(),amount,refundId,currency,status);
            return Optional.of(paymentRefunded);
        }
        return Optional.empty();
    }

    public static final class Builder{
        private String accessToken;
        private String refundLink;
        private Amount amount;
        private int idOrdine;
        private int idUtente;

        public Builder(){
            accessToken = "";
            refundLink = "";
            amount = new Amount("",0.0);
            idUtente = 0;
            idOrdine = 0;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setRefundLink(String refundLink) {
            this.refundLink = refundLink;
            return this;
        }

        public Builder setAmount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder setIdOrdine(int idOrdine) {
            this.idOrdine = idOrdine;
            return this;
        }

        public Builder setIdUtente(int idUtente) {
            this.idUtente = idUtente;
            return this;
        }

        public RefundPaymentRequest build(){
            return new RefundPaymentRequest(this);
        }
    }
}