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

public final class PaypalOrders implements Cloneable,Serializable,Comparable<PaypalOrders>,PaypalModels<PaypalOrders>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String orderId;
    private final String status;
    private final double amount;
    private final String currency;
    private final String locale;
    private final long userId;
    private final Calendar createdAt;
    private final Calendar updatedAt;

    public PaypalOrders(){
        this(new Builder());
    }

    private PaypalOrders(Builder builder){
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.status = builder.status;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.locale = builder.locale;
        this.userId = builder.userId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    private PaypalOrders(PaypalOrders original){
        this.id = original.id;
        this.orderId = original.orderId;
        this.status = original.status;
        this.amount = original.amount;
        this.currency = original.currency;
        this.locale = original.locale;
        this.userId = original.userId;
        this.createdAt = DateManagement.copyCalendar(original.createdAt);
        this.updatedAt = DateManagement.copyCalendar(original.updatedAt);
    }

    public long getId(){
        return id;
    }

    public String getOrderId(){
        return orderId;
    }

    public String getStatus(){
        return status;
    }

    public double getAmount(){
        return amount;
    }

    public String getCurrency(){
        return currency;
    }

    public String getLocale(){
        return locale;
    }

    public long getUserId(){
        return userId;
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
        return new PaypalOrders(this);
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
        if(!(obj instanceof PaypalOrders)){
            return false;
        }
        PaypalOrders other = (PaypalOrders)(obj);
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
    public int compareTo(PaypalOrders o){
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
    public PaypalOrders copy(){
        return new PaypalOrders(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("orderId",orderId);
        output.put("status",status);
        output.put("amount",amount);
        output.put("currency",currency);
        output.put("locale",locale);
        output.put("userId",userId);
        output.put("createdAt",DateManagement.printJSONCalendar(createdAt));
        output.put("updatedAt",DateManagement.printJSONCalendar(updatedAt));
        return output;
    }

    @Override
    public Optional<PaypalOrders> convertDBToJava(ResultSet row) throws SQLException{
        if(row == null){
            return Optional.empty();
        }
        long id1 = row.getLong("id");
        String orderId1 = row.getString("order_id");
        String status1 = row.getString("status");
        double amount1 = row.getDouble("amount");
        String currency1 = row.getString("currency");
        String locale1 = row.getString("locale");
        long userId1 = row.getLong("user_id");
        Timestamp createdAt1 = row.getTimestamp("created_at");
        Timestamp updatedAt1 = row.getTimestamp("updated_at");
        PaypalOrders paypalOrder = new PaypalOrders.Builder()
                .setId(id1)
                .setOrderId(orderId1)
                .setStatus(status1)
                .setAmount(amount1)
                .setCurrency(currency1)
                .setLocale(locale1)
                .setUserId(userId1)
                .setCreatedAt(DateManagement.fromDatabaseToCalendar(createdAt1))
                .setUpdatedAt(DateManagement.fromDatabaseToCalendar(updatedAt1))
                .build();
        return Optional.of(paypalOrder);
    }

    @Override
    public PaypalOrders parseObjectFromJSON(JSONObject json) throws ParserException{
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }

    public static final class Builder{
        private long id;
        private String orderId;
        private String status;
        private double amount;
        private String currency;
        private String locale;
        private long userId;
        private Calendar createdAt;
        private Calendar updatedAt;

        public Builder(){
            id = 0;
            orderId = "";
            status = "";
            amount = 0.0;
            currency = "";
            locale = "";
            userId = 0;
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

        public Builder setStatus(String status){
            this.status = status;
            return this;
        }

        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }

        public Builder setCurrency(String currency){
            this.currency = currency;
            return this;
        }

        public Builder setLocale(String locale){
            this.locale = locale;
            return this;
        }

        public Builder setUserId(long userId){
            this.userId = userId;
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

        public PaypalOrders build(){
            return new PaypalOrders(this);
        }
    }
}