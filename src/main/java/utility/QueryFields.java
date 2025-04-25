package utility;

import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class QueryFields<T extends Comparable<T>> implements Serializable,Cloneable,Comparable<QueryFields<T>>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String fieldName;
    private final T fieldValue;
    private final TipoVariabile fieldType;

    public QueryFields(String fieldName,T fieldValue,TipoVariabile fieldType) throws SQLException{
        if(fieldName == null ||  fieldValue == null ||  fieldType == null){
            throw new SQLException("Field name, value and type cannot be null.");
        }
        checkTypeConsistency(fieldValue,fieldType);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
    }

    public QueryFields(QueryFields<T> original){
        this.fieldName = original.fieldName;
        this.fieldValue = original.fieldValue;
        this.fieldType = original.fieldType;
    }

    public String getFieldName(){
        return fieldName;
    }

    public T getFieldValue(){
        return fieldValue;
    }

    public TipoVariabile getFieldType(){
        return fieldType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new QueryFields<>(this);
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
        if(!(obj.getClass().getName().equals(this.getClass().getName()))){
            return false;
        }
        QueryFields<T> other = (QueryFields<T>)(obj);
        return fieldName.equals(other.fieldName) && fieldType.equals(other.fieldType);
    }

    @Override
    public int hashCode(){
        long hash = fieldName.hashCode() + fieldValue.hashCode() + fieldType.hashCode();
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(QueryFields<T> o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        int compare1 = fieldType.compareTo(o.fieldType);
        if(compare1 == 0){
            int compare2 = fieldValue.compareTo(o.fieldValue);
            if(compare2 == 0){
                return fieldName.compareTo(o.fieldName);
            }
            return compare2;
        }
        return compare1;
    }

    public QueryFields<T> copy(){
        return new QueryFields<>(this);
    }

    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("fieldName",fieldName);
        output.put("fieldValue",fieldValue);
        output.put("fieldType",fieldType.toString());
        return output;
    }

    public Optional<QueryFields<T>> extractFromDatabase(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }
    private void checkTypeConsistency(T value,TipoVariabile type) throws SQLException{
        switch(type){
            case string:
                if(!(value instanceof String)){
                    throw new SQLException("For type " + type + " the value of the field must be a String.");
                }
                break;
            case longNumber:
                if(!(value instanceof Integer) && !(value instanceof Long)){
                    throw new SQLException("For type number the value of the field must be a Long or a Number.");
                }
                break;
            case realNumber:
                if(!(value instanceof Float) && !(value instanceof Double)){
                    throw new SQLException("For type number the value of the field must be a Long or a Number.");
                }
                break;
            default:
                throw new SQLException("Unsupported type " + type);
        }
    }
}