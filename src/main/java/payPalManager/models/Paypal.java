package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class Paypal implements Cloneable,Serializable,Comparable<Paypal>,PaypalModels<Paypal>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final ExperienceContext experienceContext;

    public Paypal(ExperienceContext experienceContext){
        this.experienceContext = experienceContext;
    }

    private Paypal(Paypal original){
        this.experienceContext = original.experienceContext.copy();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new Paypal(this);
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
        if(!(obj instanceof Paypal)){
            return false;
        }
        Paypal other = (Paypal)(obj);
        return experienceContext.equals(other.experienceContext);
    }

    @Override
    public int hashCode(){
        return experienceContext.hashCode();
    }

    @Override
    public int compareTo(Paypal o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return experienceContext.compareTo(o.experienceContext);
    }

    @Override
    public Paypal copy(){
        return new Paypal(this);
    }

    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("experience_context",experienceContext.printJSONObject());
        return output;
    }

    @Override
    public Optional<Paypal> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public Paypal parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}