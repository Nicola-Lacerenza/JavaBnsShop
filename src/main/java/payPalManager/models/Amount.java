package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class Amount implements Cloneable,Serializable,Comparable<Amount>,PaypalModels<Amount>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String currencyCode;
    private final double value;

    public Amount(String currencyCode,double value){
        this.currencyCode = currencyCode;
        this.value = value;
    }

    private Amount(Amount original){
        this.currencyCode = original.currencyCode;
        this.value = original.value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new Amount(this);
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
        if(!(obj instanceof Amount)){
            return false;
        }
        Amount other = (Amount)(obj);
        return currencyCode.equals(other.currencyCode) && value == other.value;
    }

    @Override
    public int hashCode(){
        long hash = currencyCode.hashCode() + ((int)(value));
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(Amount o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        if(value < o.value){
            return -1;
        }else if(value > o.value){
            return 1;
        }
        return currencyCode.compareTo(o.currencyCode);
    }

    @Override
    public Amount copy(){
        return new Amount(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("currency_code",currencyCode);
        output.put("value",value);
        return output;
    }

    @Override
    public Optional<Amount> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public Amount parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}