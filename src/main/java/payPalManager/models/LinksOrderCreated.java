package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class LinksOrderCreated implements Cloneable,Serializable,Comparable<LinksOrderCreated>,PaypalModels<LinksOrderCreated>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String method;
    private final String rel;
    private final String href;

    public LinksOrderCreated(){
        this("","","");
    }

    public LinksOrderCreated(String method,String rel,String href){
        this.method = method;
        this.rel = rel;
        this.href = href;
    }

    private LinksOrderCreated(LinksOrderCreated original){
        this.method = original.method;
        this.rel = original.rel;
        this.href = original.href;
    }

    public String getMethod(){
        return method;
    }

    public String getRel(){
        return rel;
    }

    public String getHref(){
        return href;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new LinksOrderCreated(this);
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
        if(!(obj instanceof LinksOrderCreated)){
            return false;
        }
        LinksOrderCreated other = (LinksOrderCreated)(obj);
        return method.equals(other.method) && rel.equals(other.rel) && href.equals(other.href);
    }

    @Override
    public int hashCode(){
        long hash = method.hashCode() + rel.hashCode() + href.hashCode();
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(LinksOrderCreated o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        int compare1 = this.rel.compareTo(o.rel);
        if(compare1 == 0){
            int compare2 = this.href.compareTo(o.href);
            if(compare2 == 0){
                return method.compareTo(o.method);
            }
            return compare2;
        }
        return compare1;
    }

    @Override
    public LinksOrderCreated copy(){
        return new LinksOrderCreated(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("method",method);
        output.put("rel",rel);
        output.put("href",href);
        return output;
    }

    @Override
    public Optional<LinksOrderCreated> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public LinksOrderCreated parseObjectFromJSON(JSONObject json) throws ParserException {
        String method1;
        String rel1;
        String href1;
        try{
            method1 = json.getString("method");
            rel1 = json.getString("rel");
            href1 = json.getString("href");
        }catch(JSONException exception){
            exception.printStackTrace();
            throw new ParserException(exception.getMessage(),exception);
        }
        return new LinksOrderCreated(method1,rel1,href1);
    }
}