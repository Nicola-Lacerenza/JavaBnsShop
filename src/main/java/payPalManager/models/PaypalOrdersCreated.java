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

public final class PaypalOrdersCreated implements Cloneable,Serializable,Comparable<PaypalOrdersCreated>,PaypalModels<PaypalOrdersCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final PaypalOrders paypalOrder;
    private final List<LinksOrderCreated> links;

    public PaypalOrdersCreated(PaypalOrders paypalOrder,List<LinksOrderCreated> links){
        this.paypalOrder = paypalOrder.copy();
        this.links = new LinkedList<>(links);
    }

    private PaypalOrdersCreated(PaypalOrdersCreated original){
        this.paypalOrder = original.paypalOrder.copy();
        this.links = new LinkedList<>(original.links);
    }

    public PaypalOrders getPaypalOrder(){
        return paypalOrder.copy();
    }

    public List<LinksOrderCreated> getLinks(){
        return new LinkedList<>(links);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalOrdersCreated(this);
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
        if(!(obj instanceof PaypalOrdersCreated)){
            return false;
        }
        PaypalOrdersCreated other = (PaypalOrdersCreated)(obj);
        return paypalOrder.equals(other.paypalOrder);
    }

    @Override
    public int hashCode(){
        return paypalOrder.hashCode();
    }

    @Override
    public int compareTo(PaypalOrdersCreated o){
        if(o == null){
            return -1;
        }
        return paypalOrder.compareTo(o.paypalOrder);
    }

    @Override
    public PaypalOrdersCreated copy(){
        return new PaypalOrdersCreated(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONArray array = new JSONArray();
        for(LinksOrderCreated link:links){
            array.put(link.printJSONObject());
        }
        JSONObject output = new JSONObject();
        output.put("paypalOrder",paypalOrder.printJSONObject());
        output.put("links",array);
        return output;
    }

    @Override
    public Optional<PaypalOrdersCreated> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PaypalOrdersCreated parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}