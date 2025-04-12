package payPalManager.utility;

import bnsshop.bnsshop.RegisterServlet;
import exceptions.ParserException;
import org.json.JSONObject;
import payPalManager.models.PaypalTokens;
import payPalManager.models.RawPaypalTokensReceived;
import utility.Database;
import utility.GestioneFileTesto;
import utility.QueryFields;
import utility.TipoVariabile;

import java.io.Serial;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CreateAPIToken extends PaypalAPIRequest<PaypalTokens>{
    @Serial
    private static final long serialVersionUID = 1L;

    public CreateAPIToken(String apiBaseURL){
        super(apiBaseURL,"");
    }

    @Override
    public String getEndpoint(){
        return "/v1/oauth2/token";
    }

    @Override
    public String getMethod(){
        return "POST";
    }

    @Override
    public Map<String,String> getHeaders(){
        //preparation of the key to do the login in PayPal system.
        Optional<String> encodedAuthorization = calculateEncodedKey();
        if(encodedAuthorization.isEmpty()){
            return new HashMap<>();
        }
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Authorization","Basic " + encodedAuthorization.get());
        return headers;
    }

    @Override
    public String getBody(){
        return "grant_type=client_credentials";
    }

    @Override
    public Optional<PaypalTokens> parseResponse(JSONObject jsonResponse){
        //conversion of the json object in a java object of the model.
        RawPaypalTokensReceived rawPaypalToken;
        try{
            RawPaypalTokensReceived templateModel = new RawPaypalTokensReceived();
            rawPaypalToken = templateModel.parseObjectFromJSON(jsonResponse);
        }catch(ParserException exception){
            exception.printStackTrace();
            return Optional.empty();
        }

        Map<Integer, QueryFields<?extends Comparable<?>>> fields = new HashMap<>();

        try {
            fields.put(0,new QueryFields<String>("access_token",jsonResponse.getString("access_token"), TipoVariabile.string));
            fields.put(1,new QueryFields<String>("scope",jsonResponse.getString("scope"), TipoVariabile.string));
            fields.put(2,new QueryFields<String>("token_type",jsonResponse.getString("token_type"), TipoVariabile.string));
            fields.put(3,new QueryFields<String>("app_id",jsonResponse.getString("app_id"), TipoVariabile.string));
            fields.put(4,new QueryFields<Integer>("expires_in",jsonResponse.getInt("expires_in"), TipoVariabile.longNumber));
            fields.put(5,new QueryFields<String>("nonce",jsonResponse.getString("nonce"), TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }

        //creation record in the database with the data arrived by PayPal.
        int id = Database.insertElementExtractId(fields,"paypal_token");
        if(id <= 0){
            System.err.println("Error inserting the token in the database.");
            return Optional.empty();
        }

        //creation output object and exit to the method.
        PaypalTokens paypalToken = new PaypalTokens.Builder()
                .setId(id)
                .setAccessToken(rawPaypalToken.getAccessToken())
                .setScope(rawPaypalToken.getScope())
                .setTokenType(rawPaypalToken.getTokenType())
                .setAppId(rawPaypalToken.getAppId())
                .setExpiresIn(rawPaypalToken.getExpiresIn())
                .setNonce(rawPaypalToken.getNonce())
                .build();
        return Optional.of(paypalToken);
    }

    private Optional<String> calculateEncodedKey(){
        Optional<Map<String,String>> keysRead = readPaypalKeys();
        if(keysRead.isEmpty()){
            return Optional.empty();
        }
        Map<String,String> keysMap = keysRead.get();
        String clientId = keysMap.get("ClientID");
        String secretKey = keysMap.get("SecretID");
        String authorization = clientId + ":" + secretKey;
        String encodedAuthorization = Base64.getEncoder().encodeToString(authorization.getBytes());
        return Optional.of(encodedAuthorization);
    }

    private static Optional<Map<String,String>> readPaypalKeys(){
        Map<String,String> keysList = GestioneFileTesto.leggiFile("PaypalKeys.txt");
        if(keysList.isEmpty()){
            return Optional.empty();
        }
        if(!keysList.containsKey("ClientID") || !keysList.containsKey("SecretID")){
            return Optional.empty();
        }
        if(keysList.get("ClientID").isEmpty() || keysList.get("SecretID").isEmpty()){

            return Optional.empty();
        }
        return Optional.of(keysList);
    }
}