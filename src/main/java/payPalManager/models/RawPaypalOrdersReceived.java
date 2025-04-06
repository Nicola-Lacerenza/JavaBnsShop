package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class RawPaypalOrdersReceived implements Cloneable,Serializable,Comparable<RawPaypalOrdersReceived>,PaypalModels<RawPaypalOrdersReceived>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String status;
    private final List<LinksOrderCreated> links;

    public RawPaypalOrdersReceived(){
        this("","",new LinkedList<>());
    }

    public RawPaypalOrdersReceived(String id,String status,List<LinksOrderCreated> links){
        this.id = id;
        this.status = status;
        this.links = links;
    }

    private RawPaypalOrdersReceived(RawPaypalOrdersReceived original){
        this.id = original.id;
        this.status = original.status;
        this.links = new LinkedList<>(original.links);
    }

    public String getId(){
        return id;
    }

    public String getStatus(){
        return status;
    }

    public List<LinksOrderCreated> getLinks(){
        return new LinkedList<>(links);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new RawPaypalOrdersReceived(this);
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
        if(!(obj instanceof RawPaypalOrdersReceived)){
            return false;
        }
        RawPaypalOrdersReceived other = (RawPaypalOrdersReceived)(obj);
        return id.equals(other.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public int compareTo(RawPaypalOrdersReceived o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return id.compareTo(o.id);
    }

    @Override
    public RawPaypalOrdersReceived copy(){
        return new RawPaypalOrdersReceived(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONArray array = new JSONArray();
        for(LinksOrderCreated link:links){
            array.put(link.printJSONObject());
        }
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("status",status);
        output.put("links",array);
        return output;
    }

    @Override
    public Optional<RawPaypalOrdersReceived> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public RawPaypalOrdersReceived parseObjectFromJSON(JSONObject json) throws ParserException{
        if(json == null){
            throw new ParserException("The input of the json parser can't be null.");
        }
        String id1;
        String status1;
        JSONArray links1;
        try{
            id1 = json.getString("id");
            status1 = json.getString("status");
            links1 = json.getJSONArray("links");
        }catch(JSONException exception){
            exception.printStackTrace();
            throw new ParserException(exception.getMessage(),exception);
        }
        List<LinksOrderCreated> list = createListFromJSONArray(links1);
        return new RawPaypalOrdersReceived(id1,status1,list);
    }

    private List<LinksOrderCreated> createListFromJSONArray(JSONArray array) throws ParserException {
        List<LinksOrderCreated> list = new LinkedList<>();
        LinksOrderCreated templateLink = new LinksOrderCreated();
        for(int i=0;i<array.length();i++){
            try{
                JSONObject object = array.getJSONObject(i);
                LinksOrderCreated link = templateLink.parseObjectFromJSON(object);
                list.add(link);
            }catch(JSONException exception){
                exception.printStackTrace();
                throw new ParserException(exception.getMessage(),exception);
            }
        }
        return list;
    }
}