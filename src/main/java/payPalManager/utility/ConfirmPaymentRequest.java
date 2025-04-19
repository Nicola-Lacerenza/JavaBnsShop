package payPalManager.utility;

import exceptions.ParserException;
import org.json.JSONObject;
import payPalManager.models.LinksOrderCreated;
import payPalManager.models.PaypalPayments;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.RawPaypalPaymentsReceived;
import utility.Database;
import utility.DateManagement;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ConfirmPaymentRequest extends PaypalAPIRequest<PaypalPaymentsCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String orderId;
    private final String payerId;

    private ConfirmPaymentRequest(Builder builder){
        super(builder.apiBaseURL,builder.accessToken);
        this.orderId = builder.orderId;
        this.payerId = builder.payerId;
    }

    @Override
    public String getEndpoint(){
        return "/v2/checkout/orders/" + orderId + "/capture";
    }

    @Override
    public String getMethod(){
        return "POST";
    }

    @Override
    public Map<String,String> getHeaders(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Authorization","Bearer " + getAccessToken());
        return headers;
    }

    @Override
    public String getBody(){
        return "{}";
    }

    @Override
    public Optional<PaypalPaymentsCreated> parseResponse(JSONObject jsonResponse){
        //conversion of the json object in a java object of the model.
        RawPaypalPaymentsReceived rawPaypalPayment;
        try{
            RawPaypalPaymentsReceived templateModel = new RawPaypalPaymentsReceived();
            rawPaypalPayment = templateModel.parseObjectFromJSON(jsonResponse);
        }catch(ParserException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        //extraction of the refund link from the full list.
        List<LinksOrderCreated> links = rawPaypalPayment.getLinks();
        LinksOrderCreated refundLink;
        int i = 0;
        while(i < links.size() && !links.get(i).getRel().equals("refund")){
            i++;
        }
        if(i >= links.size()){
            refundLink = new LinksOrderCreated();
        }else{
            refundLink = links.get(i);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }
        int idPayPalPagamentoCreato=-1;
        Connection connection = null;
        boolean output;
        try {
            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false);

            String query1 = "INSERT INTO paypal_pagamento_creato(id_ordine_paypal,payer_id,payment_id,status,paypal_fee,gross_amount,net_amount,refund_link_href,refund_link_request_method) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, orderId);
            preparedStatement1.setString(2, payerId);
            preparedStatement1.setString(3, rawPaypalPayment.getPaymentId());
            preparedStatement1.setString(4, rawPaypalPayment.getPaymentStatus());
            preparedStatement1.setDouble(5, rawPaypalPayment.getPaypalFee());
            preparedStatement1.setDouble(6, rawPaypalPayment.getGrossAmount());
            preparedStatement1.setDouble(7, rawPaypalPayment.getNetAmount());
            preparedStatement1.setString(8, refundLink.getHref());
            preparedStatement1.setString(9, refundLink.getMethod());
            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            if (generatedKeys.next()) {
                idPayPalPagamentoCreato = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Errore: Nessun ID prodotto generato.");
            }

            String query2 = "UPDATE ordine SET stato_ordine=?,data_aggiornamento_stato_ordine= NOW() WHERE id_ordine_paypal = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1, rawPaypalPayment.getOrderStatus());
            preparedStatement2.setString(2,rawPaypalPayment.getOrderId());
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
        if (idPayPalPagamentoCreato<=0 || !output){
            return Optional.empty();
        }

        //creation output object and exit to the method.
        PaypalPayments paypalPayment = new PaypalPayments.Builder()
                .setId(idPayPalPagamentoCreato)
                .setOrderId(orderId)
                .setPayerId(payerId)
                .setPaymentId(rawPaypalPayment.getPaymentId())
                .setStatus(rawPaypalPayment.getPaymentStatus())
                .setPaypalFee(rawPaypalPayment.getPaypalFee())
                .setGrossAmount(rawPaypalPayment.getGrossAmount())
                .setNetAmount(rawPaypalPayment.getNetAmount())
                .build();
        PaypalPaymentsCreated paymentCreated = new PaypalPaymentsCreated(paypalPayment,refundLink);
        return Optional.of(paymentCreated);
    }

    public static final class Builder{
        private String apiBaseURL;
        private String accessToken;
        private String orderId;
        private String payerId;

        public Builder(){
            apiBaseURL = "";
            accessToken = "";
            orderId = "";
            payerId = "";
        }

        public Builder setApiBaseURL(String apiBaseURL){
            this.apiBaseURL = apiBaseURL;
            return this;
        }

        public Builder setAccessToken(String accessToken){
            this.accessToken = accessToken;
            return this;
        }

        public Builder setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }

        public Builder setPayerId(String payerId){
            this.payerId = payerId;
            return this;
        }

        public ConfirmPaymentRequest build(){
            return new ConfirmPaymentRequest(this);
        }
    }
}