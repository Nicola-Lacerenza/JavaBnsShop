package payPalManager.utility;

import exceptions.ParserException;
import models.Taglia;
import models.TaglieProdotti;
import org.json.JSONObject;
import payPalManager.CartItem;
import payPalManager.models.LinksOrderCreated;
import payPalManager.models.PaypalPayments;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.RawPaypalPaymentsReceived;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.Serial;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ConfirmPaymentRequest extends PaypalAPIRequest<PaypalPaymentsCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String orderId;
    private final String payerId;
    private final List<CartItem> cartItems;

    private ConfirmPaymentRequest(Builder builder){
        super(builder.apiBaseURL,builder.accessToken);
        this.orderId = builder.orderId;
        this.payerId = builder.payerId;
        this.cartItems = builder.cartItems;
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

        //dati necessari per inserire il pagamento nel database (tabella paypal_pagamento_creato).
        Map<Integer,QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_ordine_paypal",orderId,TipoVariabile.string));
            fields1.put(1,new QueryFields<>("payer_id",payerId,TipoVariabile.string));
            fields1.put(2,new QueryFields<>("payment_id",rawPaypalPayment.getPaymentId(),TipoVariabile.string));
            fields1.put(3,new QueryFields<>("status",rawPaypalPayment.getPaymentStatus(),TipoVariabile.string));
            fields1.put(4,new QueryFields<>("paypal_fee",rawPaypalPayment.getPaypalFee(),TipoVariabile.realNumber));
            fields1.put(5,new QueryFields<>("gross_amount",rawPaypalPayment.getGrossAmount(),TipoVariabile.realNumber));
            fields1.put(6,new QueryFields<>("net_amount",rawPaypalPayment.getNetAmount(),TipoVariabile.realNumber));
            fields1.put(7,new QueryFields<>("refund_link_href",refundLink.getHref(),TipoVariabile.string));
            fields1.put(8,new QueryFields<>("refund_link_request_method",refundLink.getMethod(),TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        //dati da aggiornare nella tabella ordine dopo il pagamento.
        Map<Integer,QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
        try{
            fields2.put(0,new QueryFields<>("stato_ordine",rawPaypalPayment.getOrderStatus(),TipoVariabile.string));
            fields2.put(1,new QueryFields<>("id_ordine_paypal",rawPaypalPayment.getOrderId(),TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        //transazione sul database
        AtomicInteger idPayPalPagamentoCreato = new AtomicInteger();
        boolean result = Database.executeTransaction(connection -> {
            idPayPalPagamentoCreato.set(Database.insertElement(connection,"paypal_pagamento_creato",fields1));
            if(idPayPalPagamentoCreato.get() <= 0){
                return false;
            }
            String query2 = "UPDATE ordine SET stato_ordine = ?,data_aggiornamento_stato_ordine = NOW() WHERE id_ordine_paypal = ?";
            boolean result2 = Database.executeGenericUpdate(connection,query2,fields2);
            if (!result2){
                return false;
            }
            for (CartItem item : cartItems){
                Map<Integer, QueryFields<? extends Comparable<?>>> fields3 = new HashMap<>();
                try{
                    fields3.put(0,new QueryFields<>("taglia_Eu",item.getTagliaScelta(), TipoVariabile.string));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
                String query3 = "SELECT * FROM taglia WHERE taglia_Eu=?";
                List<Taglia> taglie = Database.executeGenericQuery(connection,query3,fields3,new Taglia());
                if (taglie.isEmpty()){
                    return false;
                }
                Map<Integer, QueryFields<? extends Comparable<?>>> fields4 = new HashMap<>();
                try{
                    fields4.put(0,new QueryFields<>("id_prodotto",item.getProdottiFull().getId(), TipoVariabile.longNumber));
                    fields4.put(1,new QueryFields<>("id_taglia",taglie.getFirst().getId(), TipoVariabile.longNumber));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
                String query4 = "SELECT * FROM taglie_has_prodotti WHERE id_prodotto = ? AND id_taglia = ?";
                List<TaglieProdotti> taglieProdotti = Database.executeGenericQuery(connection,query4,fields4,new TaglieProdotti());
                if (taglieProdotti.isEmpty()){
                    return false;
                }
                //dati per aggiornare la quantita.
                Map<Integer,QueryFields<? extends Comparable<?>>> fields5 = new HashMap<>();
                try{
                    fields5.put(0,new QueryFields<>("quantita",taglieProdotti.getFirst().getQuantita()-item.getQuantity(),TipoVariabile.longNumber));
                    fields5.put(1,new QueryFields<>("id_prodotto",item.getProdottiFull().getId(),TipoVariabile.longNumber));
                    fields5.put(2,new QueryFields<>("id_taglia",taglie.getFirst().getId(),TipoVariabile.longNumber));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }

                String query5 = "UPDATE taglie_has_prodotti SET quantita = ? WHERE id_prodotto=? AND id_taglia=?";
                boolean result5 = Database.executeGenericUpdate(connection,query5,fields5);
                if (!result5){
                    return false;
                }
            }
            return true;
        });

        //creation output object and exit to the method.
        if(result){
            PaypalPayments paypalPayment = new PaypalPayments.Builder()
                    .setId(idPayPalPagamentoCreato.get())
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
        return Optional.empty();
    }

    public static final class Builder{
        private String apiBaseURL;
        private String accessToken;
        private String orderId;
        private String payerId;
        private List<CartItem> cartItems;

        public Builder(){
            apiBaseURL = "";
            accessToken = "";
            orderId = "";
            payerId = "";
            cartItems = new LinkedList<>();
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
        public Builder setCartItem(List<CartItem> cartItem){
            this.cartItems = cartItem;
            return this;
        }

        public ConfirmPaymentRequest build(){
            return new ConfirmPaymentRequest(this);
        }
    }
}