package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class PurchaseUnits implements Cloneable,Serializable,PaypalModels<PurchaseUnits>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<PurchaseUnit> purchaseUnits;

    public PurchaseUnits(){
        purchaseUnits = new LinkedList<>();
    }

    private PurchaseUnits(PurchaseUnits original){
        purchaseUnits = new LinkedList<>(original.purchaseUnits);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PurchaseUnits(this);
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
        if(!(obj instanceof PurchaseUnits)){
            return false;
        }
        PurchaseUnits other = (PurchaseUnits)(obj);
        return purchaseUnits.equals(other.purchaseUnits);
    }

    @Override
    public int hashCode(){
        return purchaseUnits.hashCode();
    }

    @Override
    public PurchaseUnits copy(){
        return new PurchaseUnits(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONArray array = new JSONArray();
        for(PurchaseUnit purchaseUnit:purchaseUnits){
            array.put(purchaseUnit.printJSONObject());
        }
        JSONObject output = new JSONObject();
        output.put("array",array);
        return output;
    }

    @Override
    public Optional<PurchaseUnits> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PurchaseUnits parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }

    public boolean addPurchaseUnit(PurchaseUnit purchaseUnit){
        if(purchaseUnit == null){
            return false;
        }
        if(purchaseUnits.contains(purchaseUnit)){
            return false;
        }
        return purchaseUnits.add(purchaseUnit);
    }
}
