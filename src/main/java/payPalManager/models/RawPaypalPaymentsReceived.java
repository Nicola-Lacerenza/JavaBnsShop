package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class RawPaypalPaymentsReceived implements Cloneable,Serializable,Comparable<RawPaypalPaymentsReceived>,PaypalModels<RawPaypalPaymentsReceived>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String orderId;
    private final String orderStatus;
    private final String paymentId;
    private final String paymentStatus;
    private final double paypalFee;
    private final double grossAmount;
    private final double netAmount;
    private final List<LinksOrderCreated> links;

    public RawPaypalPaymentsReceived(){
        this(new Builder());
    }

    private RawPaypalPaymentsReceived(Builder builder){
        this.orderId = builder.orderId;
        this.orderStatus = builder.orderStatus;
        this.paymentId = builder.paymentId;
        this.paymentStatus = builder.paymentStatus;
        this.paypalFee = builder.paypalFee;
        this.grossAmount = builder.grossAmount;
        this.netAmount = builder.netAmount;
        this.links = new LinkedList<>(builder.links);
    }

    private RawPaypalPaymentsReceived(RawPaypalPaymentsReceived original){
        this.orderId = original.orderId;
        this.orderStatus = original.orderStatus;
        this.paymentId = original.paymentId;
        this.paymentStatus = original.paymentStatus;
        this.paypalFee = original.paypalFee;
        this.grossAmount = original.grossAmount;
        this.netAmount = original.netAmount;
        this.links = new LinkedList<>(original.links);
    }

    public String getOrderId(){
        return orderId;
    }

    public String getOrderStatus(){
        return orderStatus;
    }

    public String getPaymentId(){
        return paymentId;
    }

    public String getPaymentStatus(){
        return paymentStatus;
    }

    public double getPaypalFee(){
        return paypalFee;
    }

    public double getGrossAmount(){
        return grossAmount;
    }

    public double getNetAmount(){
        return netAmount;
    }

    public List<LinksOrderCreated> getLinks(){
        return new LinkedList<>(links);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        super.clone();
        return new RawPaypalPaymentsReceived(this);
    }

    @Override
    public String toString(){
        return printJSONObject().toString(4);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof RawPaypalPaymentsReceived)){
            return false;
        }
        RawPaypalPaymentsReceived other = (RawPaypalPaymentsReceived)(obj);
        return paymentId.equals(other.paymentId);
    }

    @Override
    public int hashCode(){
        return paymentId.hashCode();
    }

    @Override
    public int compareTo(RawPaypalPaymentsReceived o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return paymentId.compareTo(o.paymentId);
    }

    @Override
    public RawPaypalPaymentsReceived copy(){
        return new RawPaypalPaymentsReceived(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONArray array = new JSONArray();
        for(LinksOrderCreated link:links){
            array.put(link.printJSONObject());
        }
        JSONObject output = new JSONObject();
        output.put("orderId",orderId);
        output.put("orderStatus",orderStatus);
        output.put("paymentId",paymentId);
        output.put("paymentStatus",paymentStatus);
        output.put("paypalFee",paypalFee);
        output.put("grossAmount",grossAmount);
        output.put("netAmount",netAmount);
        output.put("links",array);
        return output;
    }

    @Override
    public Optional<RawPaypalPaymentsReceived> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public RawPaypalPaymentsReceived parseObjectFromJSON(JSONObject json) throws ParserException{
        if(json == null){
            throw new ParserException("The input of the json parser can't be null.");
        }
        JSONArray purchaseUnits;
        JSONObject purchaseUnit;
        JSONObject payments;
        JSONArray captures;
        JSONObject capture;
        JSONObject sellerReceivableBreakdown;
        JSONObject paypalFeeObject;
        JSONObject grossAmountObject;
        JSONObject netAmountFeeObject;
        String paypalFeeString;
        String grossAmountString;
        String netAmountString;

        JSONArray links1;
        String orderId1;
        String orderStatus1;
        String paymentId1;
        String paymentStatus1;
        double paypalFee1;
        double grossAmount1;
        double netAmount1;
        try{
            purchaseUnits = json.getJSONArray("purchase_units");
            purchaseUnit = purchaseUnits.getJSONObject(0);
            payments = purchaseUnit.getJSONObject("payments");
            captures = payments.getJSONArray("captures");
            capture = captures.getJSONObject(0);
            sellerReceivableBreakdown = capture.getJSONObject("seller_receivable_breakdown");
            paypalFeeObject = sellerReceivableBreakdown.getJSONObject("paypal_fee");
            grossAmountObject = sellerReceivableBreakdown.getJSONObject("gross_amount");
            netAmountFeeObject = sellerReceivableBreakdown.getJSONObject("net_amount");
            paypalFeeString = paypalFeeObject.getString("value");
            grossAmountString = grossAmountObject.getString("value");
            netAmountString = netAmountFeeObject.getString("value");

            links1 = capture.getJSONArray("links");
            orderId1 = json.getString("id");
            orderStatus1 = json.getString("status");
            paymentId1 = capture.getString("id");
            paymentStatus1 = capture.getString("status");
        }catch(JSONException exception){
            exception.printStackTrace();
            throw new ParserException(exception.getMessage(),exception);
        }
        try{
            paypalFee1 = Double.parseDouble(paypalFeeString);
            grossAmount1 = Double.parseDouble(grossAmountString);
            netAmount1 = Double.parseDouble(netAmountString);
        }catch(NumberFormatException exception){
            exception.printStackTrace();
            throw new ParserException(exception.getMessage(),exception);
        }
        List<LinksOrderCreated> list = createListFromJSONArray(links1);
        return new RawPaypalPaymentsReceived.Builder()
                .setOrderId(orderId1)
                .setOrderStatus(orderStatus1)
                .setPaymentId(paymentId1)
                .setPaymentStatus(paymentStatus1)
                .setPaypalFee(paypalFee1)
                .setGrossAmount(grossAmount1)
                .setNetAmount(netAmount1)
                .setLinks(list)
                .build();
    }

    private List<LinksOrderCreated> createListFromJSONArray(JSONArray array) throws ParserException {
        List<LinksOrderCreated> list = new LinkedList<>();
        LinksOrderCreated templateLink = new LinksOrderCreated();
        for(int i=0;i<array.length();i++){
            try{
                JSONObject object = array.getJSONObject(i);
                LinksOrderCreated link = templateLink.parseObjectFromJSON(object);
                list.add(link);
            }catch(JSONException exception){
                exception.printStackTrace();
                throw new ParserException(exception.getMessage(),exception);
            }
        }
        return list;
    }

    public static final class Builder{
        private String orderId;
        private String orderStatus;
        private String paymentId;
        private String paymentStatus;
        private double paypalFee;
        private double grossAmount;
        private double netAmount;
        private List<LinksOrderCreated> links;

        public Builder(){
            orderId = "";
            orderStatus = "";
            paymentId = "";
            paymentStatus = "";
            paypalFee = 0.0;
            grossAmount = 0.0;
            netAmount = 0.0;
            links = new LinkedList<>();
        }

        public Builder setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }

        public Builder setOrderStatus(String orderStatus){
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setPaymentId(String paymentId){
            this.paymentId = paymentId;
            return this;
        }

        public Builder setPaymentStatus(String paymentStatus){
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setPaypalFee(double paypalFee){
            this.paypalFee = paypalFee;
            return this;
        }

        public Builder setGrossAmount(double grossAmount){
            this.grossAmount = grossAmount;
            return this;
        }

        public Builder setNetAmount(double netAmount){
            this.netAmount = netAmount;
            return this;
        }

        public Builder setLinks(List<LinksOrderCreated> links){
            this.links = new LinkedList<>(links);
            return this;
        }

        public RawPaypalPaymentsReceived build(){
            return new RawPaypalPaymentsReceived(this);
        }
    }
}