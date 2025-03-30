package payPalManager;

import models.Oggetti;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PaypalToken implements Oggetti<PaypalToken>,Comparable<PaypalToken>{
    private final int id;
    private final String accessToken;
    private final String scope;
    private final String tokenType;
    private final String appId;
    private final int expiresIn;
    private final String nonce;

    public PaypalToken(){
        this(0,"","","","",0,"");
    }

    public PaypalToken(int id, String accessToken, String scope, String tokenType, String appId, int expiresIn, String nonce) {
        this.id = id;
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.appId = appId;
        this.expiresIn = expiresIn;
        this.nonce = nonce;
    }

    public int getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAppId() {
        return appId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getNonce() {
        return nonce;
    }

    @Override
    public int compareTo(PaypalToken o) {
        if (o == null){
            return -1;
        }

        if (this == o){
            return 0;
        }
        return id-o.id;
    }

    @Override
    public Optional<PaypalToken> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String accessToken = rs.getString("access_token");
            String scope = rs.getString("scope");
            String tokenType = rs.getString("token_type");
            String appId = rs.getString("app_id");
            int expiresIn = rs.getInt("expires_in");
            String nonce = rs.getString("nonce");
            return Optional.of(new PaypalToken(id1,accessToken,scope,tokenType,appId,expiresIn,nonce));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("access_token",accessToken);
        output.put("scope",scope);
        output.put("token_type",tokenType);
        output.put("app_id",appId);
        output.put("expires_in",expiresIn);
        output.put("nonce",nonce);
        return output.toString(4);
    }
}
