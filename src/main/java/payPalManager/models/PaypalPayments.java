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

public final class PaypalPayments implements Cloneable,Serializable,Comparable<PaypalPayments>,PaypalModels<PaypalPayments>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String orderId;
    private final String payerId;
    private final String paymentId;
    private final String status;
    private final double paypalFee;
    private final double grossAmount;
    private final double netAmount;
    private final Calendar createdAt;
    private final Calendar updatedAt;

    public PaypalPayments(){
        this(new Builder());
    }

    private PaypalPayments(Builder builder){
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.payerId = builder.payerId;
        this.paymentId = builder.paymentId;
        this.status = builder.status;
        this.paypalFee = builder.paypalFee;
        this.grossAmount = builder.grossAmount;
        this.netAmount = builder.netAmount;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    private PaypalPayments(PaypalPayments original){
        this.id = original.id;
        this.orderId = original.orderId;
        this.payerId = original.payerId;
        this.paymentId = original.paymentId;
        this.status = original.status;
        this.paypalFee = original.paypalFee;
        this.grossAmount = original.grossAmount;
        this.netAmount = original.netAmount;
        this.createdAt = DateManagement.copyCalendar(original.createdAt);
        this.updatedAt = DateManagement.copyCalendar(original.updatedAt);
    }

    public long getId(){
        return id;
    }

    public String getOrderId(){
        return orderId;
    }

    public String getPayerId(){
        return payerId;
    }

    public String getPaymentId(){
        return paymentId;
    }

    public String getStatus(){
        return status;
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

    public Calendar getCreatedAt(){
        return DateManagement.copyCalendar(createdAt);
    }

    public Calendar getUpdatedAt(){
        return DateManagement.copyCalendar(updatedAt);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalPayments(this);
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
        if(!(obj instanceof PaypalPayments)){
            return false;
        }
        PaypalPayments other = (PaypalPayments)(obj);
        return id == other.id;
    }

    @Override
    public int hashCode(){
        if(id <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(id >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(id);
    }

    @Override
    public int compareTo(PaypalPayments o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        if(id < o.id){
            return -1;
        }else if(id > o.id){
            return 1;
        }
        return 0;
    }

    @Override
    public PaypalPayments copy(){
        return new PaypalPayments(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("orderId",orderId);
        output.put("payerId",payerId);
        output.put("paymentId",paymentId);
        output.put("status",status);
        output.put("paypalFee",paypalFee);
        output.put("grossAmount",grossAmount);
        output.put("netAmount",netAmount);
        output.put("createdAt",DateManagement.printJSONCalendar(createdAt));
        output.put("updatedAt",DateManagement.printJSONCalendar(updatedAt));
        return output;
    }

    @Override
    public Optional<PaypalPayments> convertDBToJava(ResultSet row) throws SQLException{
        if(row == null){
            return Optional.empty();
        }
        long id1 = row.getLong("id");
        String orderId1 = row.getString("order_id");
        String payerId1 = row.getString("payer_id");
        String paymentId1 = row.getString("payment_id");
        String status1 = row.getString("status");
        double paypalFee1 = row.getDouble("paypal_fee");
        double grossAmount1 = row.getDouble("gross_amount");
        double netAmount1 = row.getDouble("net_amount");
        Timestamp createdAt1 = row.getTimestamp("created_at");
        Timestamp updatedAt1 = row.getTimestamp("updated_at");
        PaypalPayments paypalPayment = new PaypalPayments.Builder()
                .setId(id1)
                .setOrderId(orderId1)
                .setPayerId(payerId1)
                .setPaymentId(paymentId1)
                .setStatus(status1)
                .setPaypalFee(paypalFee1)
                .setGrossAmount(grossAmount1)
                .setNetAmount(netAmount1)
                .setCreatedAt(DateManagement.fromDatabaseToCalendar(createdAt1))
                .setUpdatedAt(DateManagement.fromDatabaseToCalendar(updatedAt1))
                .build();
        return Optional.of(paypalPayment);
    }

    @Override
    public PaypalPayments parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }

    public static final class Builder{
        private long id;
        private String orderId;
        private String payerId;
        private String paymentId;
        private String status;
        private double paypalFee;
        private double grossAmount;
        private double netAmount;
        private Calendar createdAt;
        private Calendar updatedAt;

        public Builder(){
            id = 0;
            orderId = "";
            payerId = "";
            paymentId = "";
            status = "";
            paypalFee = 0.0;
            grossAmount = 0.0;
            netAmount = 0.0;
            createdAt = new GregorianCalendar();
            updatedAt = new GregorianCalendar();
        }

        public Builder setId(long id){
            this.id = id;
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

        public Builder setPaymentId(String paymentId){
            this.paymentId = paymentId;
            return this;
        }

        public Builder setStatus(String status){
            this.status = status;
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

        public Builder setCreatedAt(Calendar createdAt){
            this.createdAt = DateManagement.copyCalendar(createdAt);
            return this;
        }

        public Builder setUpdatedAt(Calendar updatedAt){
            this.updatedAt = DateManagement.copyCalendar(updatedAt);
            return this;
        }

        public PaypalPayments build(){
            return new PaypalPayments(this);
        }
    }
}