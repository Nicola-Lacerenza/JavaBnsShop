package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class PaypalConfirmPaymentInput implements Cloneable,Serializable,PaypalModels<PaypalConfirmPaymentInput>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String token;
    private final String payerId;

    public PaypalConfirmPaymentInput(String token,String payerId){
        this.token = token;
        this.payerId = payerId;
    }

    private PaypalConfirmPaymentInput(PaypalConfirmPaymentInput original){
        this.token = original.token;
        this.payerId = original.payerId;
    }

    public String getToken(){
        return token;
    }

    public String getPayerId(){
        return payerId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalConfirmPaymentInput(this);
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
        if(!(obj instanceof PaypalConfirmPaymentInput)){
            return false;
        }
        PaypalConfirmPaymentInput other = (PaypalConfirmPaymentInput)(obj);
        return token.equals(other.token) && payerId.equals(other.payerId);
    }

    @Override
    public int hashCode(){
        long hash = token.hashCode() + payerId.hashCode();
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public PaypalConfirmPaymentInput copy(){
        return new PaypalConfirmPaymentInput(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("token",token);
        output.put("payerId",payerId);
        return output;
    }

    @Override
    public Optional<PaypalConfirmPaymentInput> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PaypalConfirmPaymentInput parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}