package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serializable;
import java.io.Serial;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class PaypalTokens implements Cloneable,Serializable,Comparable<PaypalTokens>,PaypalModels<PaypalTokens>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String accessToken;
    private final String scope;
    private final String tokenType;
    private final String appId;
    private final long expiresIn;
    private final String nonce;

    public PaypalTokens(){
        this(new Builder());
    }

    private PaypalTokens(Builder builder){
        this.id = builder.id;
        this.accessToken = builder.accessToken;
        this.scope = builder.scope;
        this.tokenType = builder.tokenType;
        this.appId = builder.appId;
        this.expiresIn = builder.expiresIn;
        this.nonce = builder.nonce;
    }

    private PaypalTokens(PaypalTokens original){
        this.id = original.id;
        this.accessToken = original.accessToken;
        this.scope = original.scope;
        this.tokenType = original.tokenType;
        this.appId = original.appId;
        this.expiresIn = original.expiresIn;
        this.nonce = original.nonce;
    }

    public long getId(){
        return id;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getScope(){
        return scope;
    }

    public String getTokenType(){
        return tokenType;
    }

    public String getAppId(){
        return appId;
    }

    public long getExpiresIn(){
        return expiresIn;
    }

    public String getNonce(){
        return nonce;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalTokens(this);
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
        if(!(obj instanceof PaypalTokens)){
            return false;
        }
        PaypalTokens other = (PaypalTokens)(obj);
        return id == other.id;
    }

    @Override
    public int hashCode(){
        if(id <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(id >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(id);
    }

    @Override
    public int compareTo(PaypalTokens o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        if(id < o.id){
            return -1;
        }else if(id > o.id){
            return 1;
        }
        return 0;
    }

    public PaypalTokens copy(){
        return new PaypalTokens(this);
    }

    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("accessToken",accessToken);
        output.put("scope",scope);
        output.put("tokenType",tokenType);
        output.put("appId",appId);
        output.put("expiresIn",expiresIn);
        output.put("nonce",nonce);
        return output;
    }

    public Optional<PaypalTokens> convertDBToJava(ResultSet row) throws SQLException{
        if(row == null){
            return Optional.empty();
        }
        long id1 = row.getLong("id");
        String accessToken1 = row.getString("access_token");
        String scope1 = row.getString("scope");
        String tokenType1 = row.getString("token_type");
        String appId1 = row.getString("app_id");
        long expiresIn1 = row.getLong("expires_in");
        String nonce1 = row.getString("nonce");
        PaypalTokens paypalToken = new PaypalTokens.Builder()
                .setId(id1)
                .setAccessToken(accessToken1)
                .setScope(scope1)
                .setTokenType(tokenType1)
                .setAppId(appId1)
                .setExpiresIn(expiresIn1)
                .setNonce(nonce1)
                .build();
        return Optional.of(paypalToken);
    }

    @Override
    public PaypalTokens parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }

    public static final class Builder{
        private long id;
        private String accessToken;
        private String scope;
        private String tokenType;
        private String appId;
        private long expiresIn;
        private String nonce;

        public Builder(){
            id = 0L;
            accessToken = "";
            scope = "";
            tokenType = "";
            appId = "";
            expiresIn = 0L;
            nonce = "";
        }

        public Builder setId(long id){
            this.id = id;
            return this;
        }

        public Builder setAccessToken(String accessToken){
            this.accessToken = accessToken;
            return this;
        }

        public Builder setScope(String scope){
            this.scope = scope;
            return this;
        }

        public Builder setTokenType(String tokenType){
            this.tokenType = tokenType;
            return this;
        }

        public Builder setAppId(String appId){
            this.appId = appId;
            return this;
        }

        public Builder setExpiresIn(long expiresIn){
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder setNonce(String nonce){
            this.nonce = nonce;
            return this;
        }

        public PaypalTokens build(){
            return new PaypalTokens(this);
        }
    }
}