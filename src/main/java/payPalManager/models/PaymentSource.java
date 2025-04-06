package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class PaymentSource implements Cloneable,Serializable,Comparable<PaymentSource>,PaypalModels<PaymentSource>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final Paypal paypal;

    public PaymentSource(Paypal paypal){
        this.paypal = paypal;
    }

    private PaymentSource(PaymentSource original){
        this.paypal = original.paypal.copy();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaymentSource(this);
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
        if(!(obj instanceof PaymentSource)){
            return false;
        }
        PaymentSource other = (PaymentSource)(obj);
        return paypal.equals(other.paypal);
    }

    @Override
    public int hashCode(){
        return paypal.hashCode();
    }

    @Override
    public int compareTo(PaymentSource o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return paypal.compareTo(o.paypal);
    }

    @Override
    public PaymentSource copy(){
        return new PaymentSource(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("paypal",paypal.printJSONObject());
        return output;
    }

    @Override
    public Optional<PaymentSource> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PaymentSource parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}