package gmailManager.utility;

import gmailManager.models.GmailToken;
import gmailManager.models.LoginData;
import org.json.JSONObject;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CreateTokenRequest extends GmailAPIRequest<GmailToken>{

    private LoginData loginData;

    private String authorizationCode;

    public CreateTokenRequest(LoginData loginData,String authorizationCode) {
        super("");
        this.loginData= loginData;
        this.authorizationCode=authorizationCode;
    }

    @Override
    public String getEndpoint() {
        return loginData.getTokenUri();
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        return headers;
    }

    @Override
    public String getBody() {
        String form = "code="+ URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8) + "&";
        form += "client_id=" + URLEncoder.encode(loginData.getClientId(),StandardCharsets.UTF_8) + "&";
        form += "client_secret=" + URLEncoder.encode(loginData.getClientSecret(),StandardCharsets.UTF_8) + "&";
        form += "redirect_uri=" + URLEncoder.encode("https://localhost:4200/ricevi-codice-google",StandardCharsets.UTF_8) + "&";
        form += "grant_type=authorization_code";
        return form;
    }

    @Override
    public Optional<GmailToken> parseResponse(JSONObject jsonResponse) {
        String token = jsonResponse.getString("access_token");
        int scadenzaToken =jsonResponse.getInt("expires_in");
        String refreshToken = jsonResponse.getString("refresh_token");
        int scadenzaRefreshToken =jsonResponse.getInt("refresh_token_expires_in");
        String scope =jsonResponse.getString("scope");
        String tipoToken = jsonResponse.getString("token_type");

        int idToken;
        try(Connection connection = Database.createConnection()){
            Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
            fields.put(0,new QueryFields<>("token",token, TipoVariabile.string));
            fields.put(1,new QueryFields<>("scadenza_token",scadenzaToken, TipoVariabile.longNumber));
            fields.put(2,new QueryFields<>("refresh_token",refreshToken, TipoVariabile.string));
            fields.put(3,new QueryFields<>("scadenza_refresh_token",scadenzaRefreshToken, TipoVariabile.longNumber));
            fields.put(4,new QueryFields<>("scope",scope, TipoVariabile.string));
            fields.put(5,new QueryFields<>("tipo_token",tipoToken, TipoVariabile.string));
            idToken = Database.insertElement(connection,"gmail_token",fields);
        }catch (SQLException e){
            e.printStackTrace();
            idToken=-1;
        }
        if (idToken<=0){
            return Optional.empty();
        }
        GmailToken tokenGenerato = new GmailToken(idToken,token,scadenzaToken,refreshToken,scadenzaRefreshToken,scope,tipoToken,new GregorianCalendar());
        return Optional.of(tokenGenerato);
    }
}
