package payPalManager.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import payPalManager.models.Amount;
import payPalManager.models.PaypalPaymentRefunded;
import utility.Database;

import java.io.Serial;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        String refundId = jsonResponse.getString("id");
        String status = jsonResponse.getString("status");
        JSONObject amountObject = jsonResponse.getJSONObject("amount");
        double amount = amountObject.getDouble("value");
        String currency = amountObject.getString("currency_code");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        int idReso=-1;
        Connection connection = null;
        boolean output;
        try {
            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false);

            String query1 = "INSERT INTO resi(id_utente,id_ordine,importo,refund_id,valuta,stato_reso) " +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setInt(1,idUtente);
            preparedStatement1.setInt(2, idOrdine);
            preparedStatement1.setDouble(3,amount);
            preparedStatement1.setString(4, refundId);
            preparedStatement1.setString(5, currency);
            preparedStatement1.setString(6, status);
            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            if (generatedKeys.next()) {
                idReso = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Errore: Nessun ID reso generato.");
            }

            String query2 = "UPDATE ordine SET stato_ordine=?,data_aggiornamento_stato_ordine= NOW() WHERE id_ordine_paypal = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1,"REFUNDED");
            preparedStatement2.setInt(2,idUtente);


            preparedStatement2.executeUpdate();

            connection.commit();
            output = true;

        } catch(SQLException e){
            // Gestione errori e rollback in caso di fallimento
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            output = false;
        } finally {
            // Chiusura connessione e statement
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        PaypalPaymentRefunded paymentRefunded = new PaypalPaymentRefunded();
        return Optional.of(paymentRefunded);
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