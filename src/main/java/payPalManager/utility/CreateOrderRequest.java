package payPalManager.utility;

import exceptions.ParserException;
import org.json.JSONObject;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private CreateOrderRequest(Builder builder){
        super(builder.apiBaseURL,builder.accessToken);
        this.userId = builder.userId;
        this.indirizzoId = builder.indirizzoId;
        this.currency = builder.currency;
        this.amount = builder.amount;
        this.locale = builder.locale;
        this.returnUrl = builder.returnUrl;
        this.cancelUrl = builder.cancelUrl;
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

        Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
        try {
            fields.put(0, new QueryFields<>("id_utente", userId, TipoVariabile.longNumber));
            fields.put(1, new QueryFields<>("id_ordine_paypal", rawPaypalOrder.getId(), TipoVariabile.string));
            fields.put(2, new QueryFields<>("id_pagamento",1, TipoVariabile.longNumber));
            fields.put(3, new QueryFields<>("id_indirizzo", 1, TipoVariabile.longNumber));
            //fields.put(3, new QueryFields<>("id_indirizzo", indirizzoId, TipoVariabile.longNumber));
            fields.put(4, new QueryFields<>("stato_ordine", rawPaypalOrder.getStatus(), TipoVariabile.string));
            fields.put(5, new QueryFields<>("importo", amount, TipoVariabile.realNumber));
            fields.put(6, new QueryFields<>("valuta", currency, TipoVariabile.string));
            fields.put(7, new QueryFields<>("locale_utente", locale, TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }

        //creation record in the database with the data arrived by PayPal or parameters of the method.
        int id = Database.insertElementExtractId(fields,"ordine");

        if(id <= 0){
            System.err.println("Error inserting the order in the database.");
            return Optional.empty();
        }

        //creation output object and exit to the method.
        PaypalOrders paypalOrder = new PaypalOrders.Builder()
                .setId(id)
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

        public CreateOrderRequest build(){
            return new CreateOrderRequest(this);
        }
    }
}