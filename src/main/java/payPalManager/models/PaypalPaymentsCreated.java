package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class PaypalPaymentsCreated implements Cloneable,Serializable,Comparable<PaypalPaymentsCreated>,PaypalModels<PaypalPaymentsCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final PaypalPayments paypalPayment;
    private final LinksOrderCreated refundLink;

    public PaypalPaymentsCreated(PaypalPayments paypalPayment,LinksOrderCreated refundLink){
        this.paypalPayment = paypalPayment.copy();
        this.refundLink = refundLink.copy();
    }

    private PaypalPaymentsCreated(PaypalPaymentsCreated original){
        this.paypalPayment = original.paypalPayment.copy();
        this.refundLink = original.refundLink.copy();
    }

    public PaypalPayments getPaypalPayment(){
        return paypalPayment.copy();
    }

    public LinksOrderCreated getRefundLink(){
        return refundLink.copy();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalPaymentsCreated(this);
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
        if(!(obj instanceof PaypalPaymentsCreated)){
            return false;
        }
        PaypalPaymentsCreated other = (PaypalPaymentsCreated)(obj);
        return paypalPayment.equals(other.paypalPayment);
    }

    @Override
    public int hashCode(){
        return paypalPayment.hashCode();
    }

    @Override
    public int compareTo(PaypalPaymentsCreated o){
        if(o == null){
            return -1;
        }
        return paypalPayment.compareTo(o.paypalPayment);
    }

    @Override
    public PaypalPaymentsCreated copy(){
        return new PaypalPaymentsCreated(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("paypalPayment",paypalPayment.printJSONObject());
        output.put("refundLink",refundLink.printJSONObject());
        return output;
    }

    @Override
    public Optional<PaypalPaymentsCreated> convertDBToJava(ResultSet row) throws SQLException {
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public PaypalPaymentsCreated parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}