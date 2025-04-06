package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class OrderData implements Cloneable,Serializable,Comparable<OrderData>,PaypalModels<OrderData>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String intent;
    private final PaymentSource paymentSource;
    private final PurchaseUnits purchaseUnits;

    public OrderData(PaymentSource paymentSource,PurchaseUnits purchaseUnits){
        this.intent = "CAPTURE";
        this.paymentSource = paymentSource;
        this.purchaseUnits = purchaseUnits;
    }

    private OrderData(OrderData original){
        this.intent = original.intent;
        this.paymentSource = original.paymentSource.copy();
        this.purchaseUnits = original.purchaseUnits.copy();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new OrderData(this);
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
        if(!(obj instanceof OrderData)){
            return false;
        }
        OrderData other = (OrderData)(obj);
        return paymentSource.equals(other.paymentSource) && purchaseUnits.equals(other.purchaseUnits);
    }

    @Override
    public int hashCode(){
        long hash = paymentSource.hashCode() + purchaseUnits.hashCode();
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(OrderData o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return paymentSource.compareTo(o.paymentSource);
    }

    @Override
    public OrderData copy(){
        return new OrderData(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject purchaseUnitsJSON = purchaseUnits.printJSONObject();
        JSONArray array = purchaseUnitsJSON.getJSONArray("array");
        JSONObject output = new JSONObject();
        output.put("intent",intent);
        output.put("payment_source",paymentSource.printJSONObject());
        output.put("purchase_units",array);
        return output;
    }

    @Override
    public Optional<OrderData> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public OrderData parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}