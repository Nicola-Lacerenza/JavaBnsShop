package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class PurchaseUnit implements Cloneable,Serializable,Comparable<PurchaseUnit>,PaypalModels<PurchaseUnit>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final Amount amount;

    public PurchaseUnit(Amount amount){
        this.amount = amount;
    }

    private PurchaseUnit(PurchaseUnit original){
        this.amount = original.amount.copy();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PurchaseUnit(this);
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
        if(!(obj instanceof PurchaseUnit)){
            return false;
        }
        PurchaseUnit other = (PurchaseUnit)(obj);
        return amount.equals(other.amount);
    }

    @Override
    public int hashCode(){
        return amount.hashCode();
    }

    @Override
    public int compareTo(PurchaseUnit o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return amount.compareTo(o.amount);
    }

    @Override
    public PurchaseUnit copy(){
        return new PurchaseUnit(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("amount",amount.printJSONObject());
        return output;
    }

    @Override
    public Optional<PurchaseUnit> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PurchaseUnit parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}