package payPalManager.utility;

import exceptions.ParserException;
import org.json.JSONObject;
import payPalManager.CartItem;
import payPalManager.models.Amount;
import payPalManager.models.ExperienceContext;
import payPalManager.models.OrderData;
import payPalManager.models.PaymentSource;
import payPalManager.models.Paypal;
import payPalManager.models.PaypalOrders;
import payPalManager.models.PaypalOrdersCreated;
import payPalManager.models.PurchaseUnit;
import payPalManager.models.PurchaseUnits;
import payPalManager.models.RawPaypalOrdersReceived;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.Serial;
import java.sql.*;
import java.util.*;

public final class CreateOrderRequest extends PaypalAPIRequest<PaypalOrdersCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final int userId;
    private final int indirizzoId;
    private final String currency;
    private final double amount;
    private final String locale;
    private final String returnUrl;
    private final String cancelUrl;

    private final List<CartItem> oggettiCarrello;

    private CreateOrderRequest(Builder builder){
        super(builder.apiBaseURL,builder.accessToken);
        this.userId = builder.userId;
        this.indirizzoId = builder.indirizzoId;
        this.currency = builder.currency;
        this.amount = builder.amount;
        this.locale = builder.locale;
        this.returnUrl = builder.returnUrl;
        this.cancelUrl = builder.cancelUrl;
        this.oggettiCarrello = builder.oggettiCarrello;
    }

    @Override
    public String getEndpoint(){
        return "/v2/checkout/orders";
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
        //creation of the message body in json format to send in the https request.
        OrderData orderData = createOrderData();
        JSONObject jsonRequest = orderData.printJSONObject();
        return jsonRequest.toString(4);
    }

    @Override
    public Optional<PaypalOrdersCreated> parseResponse(JSONObject jsonResponse){
        //conversion of the json object in a java object of the model.
        RawPaypalOrdersReceived rawPaypalOrder;
        try{
            RawPaypalOrdersReceived templateModel = new RawPaypalOrdersReceived();
            rawPaypalOrder = templateModel.parseObjectFromJSON(jsonResponse);
        }catch(ParserException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }
        int idOrdine = -1;

        Connection connection = null;
        List<PreparedStatement> statementList = new LinkedList<>();
        boolean output = false;
        try {

            // APERTURA CONNESSIONE

            connection = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connection.setAutoCommit(false); // Avvia transazione

            String query1 = "INSERT INTO ordine(id_utente,id_ordine_paypal,id_pagamento,id_indirizzo,stato_ordine,importo,valuta,locale_utente) " +
                    "VALUES(?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setInt(1, userId);
            preparedStatement1.setString(2, rawPaypalOrder.getId());
            preparedStatement1.setInt(3, 1);
            preparedStatement1.setInt(4, 1);
            preparedStatement1.setString(5, rawPaypalOrder.getStatus());
            preparedStatement1.setDouble(6, amount);
            preparedStatement1.setString(7, currency);
            preparedStatement1.setString(8, locale);
            preparedStatement1.executeUpdate();

            ResultSet rs1 = preparedStatement1.getGeneratedKeys();
            if (rs1.next()) {
                idOrdine = rs1.getInt(1);
            }else{
                throw new SQLException("Errore Durante l'inserimento");
            }

            for (CartItem oggetto : oggettiCarrello){
                String query2 = "INSERT INTO dettagli_ordine(id_ordine,id_prodotto,quantita,prezzo,taglia_scelta) " +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setInt(1, idOrdine);
                preparedStatement2.setInt(2, oggetto.getProdottiFull().getId() );
                preparedStatement2.setInt(3, oggetto.getQuantity());
                preparedStatement2.setDouble(4, oggetto.getProdottiFull().getPrezzo());
                preparedStatement2.setString(5, oggetto.getTagliaScelta());
                preparedStatement2.executeUpdate();

            }

            connection.commit();
            output = true;
        } catch (SQLException e) {

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

            //creation output object and exit to the method.
        PaypalOrders paypalOrder = new PaypalOrders.Builder()
                .setId(idOrdine)
                .setOrderId(rawPaypalOrder.getId())
                .setStatus(rawPaypalOrder.getStatus())
                .setAmount(amount)
                .setCurrency(currency)
                .setLocale(locale)
                .setUserId(userId)
                .setIdIndirizzo(indirizzoId)
                .build();
        PaypalOrdersCreated orderCreated = new PaypalOrdersCreated(paypalOrder,rawPaypalOrder.getLinks());
        return Optional.of(orderCreated);
    }

    private OrderData createOrderData(){
        ExperienceContext experienceContext = new ExperienceContext(locale,returnUrl,cancelUrl);
        Paypal paypal = new Paypal(experienceContext);
        PaymentSource paymentSource = new PaymentSource(paypal);
        PurchaseUnits purchaseUnits = new PurchaseUnits();
        Amount amountObj = new Amount(currency,amount);
        PurchaseUnit purchaseUnit = new PurchaseUnit(amountObj);
        if(purchaseUnits.addPurchaseUnit(purchaseUnit)){
            System.out.println("Purchase unit added correctly to the order.");
        }
        return new OrderData(paymentSource,purchaseUnits);
    }

    public static final class Builder{
        private String apiBaseURL;
        private String accessToken;
        private int userId;
        private int indirizzoId;
        private String currency;
        private double amount;
        private String locale;
        private String returnUrl;
        private String cancelUrl;
        private List<CartItem> oggettiCarrello;

        public Builder(){
            apiBaseURL = "";
            accessToken = "";
            userId = -1;
            indirizzoId = -1;
            currency = "";
            amount = 0.0;
            locale = "";
            returnUrl = "";
            cancelUrl = "";
            oggettiCarrello = new LinkedList<>();
        }

        public Builder setApiBaseURL(String apiBaseURL){
            this.apiBaseURL = apiBaseURL;
            return this;
        }

        public Builder setAccessToken(String accessToken){
            this.accessToken = accessToken;
            return this;
        }

        public Builder setUserId(int userId){
            this.userId = userId;
            return this;
        }

        public Builder setIndirizzoId(int indirizzoId){
            this.indirizzoId = indirizzoId;
            return this;
        }

        public Builder setCurrency(String currency){
            this.currency = currency;
            return this;
        }

        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }

        public Builder setLocale(String locale){
            this.locale = locale;
            return this;
        }

        public Builder setReturnUrl(String returnUrl){
            this.returnUrl = returnUrl;
            return this;
        }

        public Builder setCancelUrl(String cancelUrl){
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder setOggettiCarrello(List<CartItem> oggettiCarrello){
            this.oggettiCarrello = new LinkedList<>(oggettiCarrello);
            return this;
        }

        public CreateOrderRequest build(){
            return new CreateOrderRequest(this);
        }
    }
}