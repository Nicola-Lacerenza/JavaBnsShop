package payPalManager.utility;

import exceptions.ParserException;
import org.json.JSONObject;
import payPalManager.models.LinksOrderCreated;
import payPalManager.models.PaypalPayments;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.RawPaypalPaymentsReceived;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
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

        //creation of the json object to insert payment created in the database.
        JSONObject creationPayment = new JSONObject();
        creationPayment.put("order_id",orderId);
        creationPayment.put("payer_id",payerId);
        creationPayment.put("payment_id",rawPaypalPayment.getPaymentId());
        creationPayment.put("status",rawPaypalPayment.getPaymentStatus());
        creationPayment.put("paypal_fee",rawPaypalPayment.getPaypalFee());
        creationPayment.put("gross_amount",rawPaypalPayment.getGrossAmount());
        creationPayment.put("net_amount",rawPaypalPayment.getNetAmount());

        //creation record in the database with the data arrived by PayPal or parameters of the method.
        long databaseId;
        /*try(DatabaseExecutor database = new DatabaseExecutor("root","localhost",3306,"..............")){
            try{
                database.setAutoCommit();
                String query = "UPDATE paypal_orders SET status = ?,updated_at = NOW() WHERE order_id = ?";
                Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
                QueryFields<String> field1 = new QueryFields<>("status",rawPaypalPayment.getOrderStatus(), ColumnType.string);
                QueryFields<String> field2 = new QueryFields<>("order_id",orderId,ColumnType.string);
                fields.put(1,field1);
                fields.put(2,field2);
                database.executeGenericUpdate(query,fields);
                databaseId = database.insertElement(new PaypalPaymentsTableSchema(),creationPayment);
                database.commitQueries();
            }catch(SQLException exception){
                database.rollbackTransaction();
                exception.printStackTrace();
                return Optional.empty();
            }
        }catch(DatabaseException exception){
            exception.printStackTrace();
            return Optional.empty();
        }*/

        //creation output object and exit to the method.
        PaypalPayments paypalPayment = new PaypalPayments.Builder()
                .setId(databaseId)
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