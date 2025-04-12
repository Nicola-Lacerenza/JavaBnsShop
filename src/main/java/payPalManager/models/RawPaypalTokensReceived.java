package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.io.Serial;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class RawPaypalTokensReceived implements Cloneable,Serializable,Comparable<RawPaypalTokensReceived>,PaypalModels<RawPaypalTokensReceived>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String accessToken;
    private final String scope;
    private final String tokenType;
    private final String appId;
    private final int expiresIn;
    private final String nonce;

    public RawPaypalTokensReceived(){
        this(new Builder());
    }

    private RawPaypalTokensReceived(Builder builder){
        this.accessToken = builder.accessToken;
        this.scope = builder.scope;
        this.tokenType = builder.tokenType;
        this.appId = builder.appId;
        this.expiresIn = builder.expiresIn;
        this.nonce = builder.nonce;
    }

    private RawPaypalTokensReceived(RawPaypalTokensReceived original){
        this.accessToken = original.accessToken;
        this.scope = original.scope;
        this.tokenType = original.tokenType;
        this.appId = original.appId;
        this.expiresIn = original.expiresIn;
        this.nonce = original.nonce;
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

    public int getExpiresIn(){
        return expiresIn;
    }

    public String getNonce(){
        return nonce;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new RawPaypalTokensReceived(this);
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
        if(!(obj instanceof RawPaypalTokensReceived)){
            return false;
        }
        RawPaypalTokensReceived other = (RawPaypalTokensReceived)(obj);
        return accessToken.equals(other.accessToken);
    }

    @Override
    public int hashCode(){
        return accessToken.hashCode();
    }

    @Override
    public int compareTo(RawPaypalTokensReceived o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        return accessToken.compareTo(o.accessToken);
    }

    public RawPaypalTokensReceived copy(){
        return new RawPaypalTokensReceived(this);
    }

    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("accessToken",accessToken);
        output.put("scope",scope);
        output.put("tokenType",tokenType);
        output.put("appId",appId);
        output.put("expiresIn",expiresIn);
        output.put("nonce",nonce);
        return output;
    }
    @Override
    public Optional<RawPaypalTokensReceived> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public RawPaypalTokensReceived parseObjectFromJSON(JSONObject json) throws ParserException {
        if(json == null){
            throw new ParserException("The input of the json parser can't be null.");
        }
        String accessToken1;
        String scope1;
        String tokenType1;
        String appId1;
        int expiresIn1;
        String nonce1;
        try{
            accessToken1 = json.getString("access_token");
            scope1 = json.getString("scope");
            tokenType1 = json.getString("token_type");
            appId1 = json.getString("app_id");
            expiresIn1 = json.getInt("expires_in");
            nonce1 = json.getString("nonce");
        }catch(JSONException exception){
            exception.printStackTrace();
            throw new ParserException(exception.getMessage(),exception);
        }
        return new RawPaypalTokensReceived.Builder()
                .setAccessToken(accessToken1)
                .setScope(scope1)
                .setTokenType(tokenType1)
                .setAppId(appId1)
                .setExpiresIn(expiresIn1)
                .setNonce(nonce1)
                .build();
    }

    public static final class Builder{
        private String accessToken;
        private String scope;
        private String tokenType;
        private String appId;
        private int expiresIn;
        private String nonce;

        public Builder(){
            accessToken = "";
            scope = "";
            tokenType = "";
            appId = "";
            expiresIn = 0;
            nonce = "";
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

        public Builder setExpiresIn(int expiresIn){
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder setNonce(String nonce){
            this.nonce = nonce;
            return this;
        }

        public RawPaypalTokensReceived build(){
            return new RawPaypalTokensReceived(this);
        }
    }
}