package gmailManager.utility;

import controllers.Controllers;
import gmailManager.controllers.GmailTokenController;
import gmailManager.models.GmailToken;
import gmailManager.models.LoginData;
import org.json.JSONObject;
import utility.GestioneFileTesto;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

public class ReadLoginData {

    public static Optional<GmailToken> creaGmailToken(String authorizationCode){
        LoginData loginData = readData();
        GmailAPIRequest<GmailToken> request = new CreateTokenRequest(loginData,authorizationCode);
        return request.execute();
    }

    public static LoginData readData() {
        JSONObject object = GestioneFileTesto.leggiFileJSON("GmailKeys.json");
        LoginData empty = new LoginData();
        return empty.parseObjectFromJSON(object);
    }

    public static Optional<GmailToken> getValidToken(){
        Controllers<GmailToken> controller = new GmailTokenController();
        List<GmailToken> tokens = controller.getAllObjects();
        Collections.sort(tokens);
        if (tokens.isEmpty()){
            return Optional.empty();
        }
        if (isAccessTokenValid(tokens.getLast())){
            return Optional.of(tokens.getLast());
        }
        if (isRefreshTokenValid(tokens.getLast())){
            LoginData loginData = readData();
            GmailAPIRequest<GmailToken> request = new RefreshTokenRequest(loginData,tokens.getLast());
            return request.execute();
        }
        return Optional.empty();
    }

    public static boolean isAccessTokenValid(GmailToken token){
        if (token==null){
            return false;
        }
        long secondiScadenza = token.getScadenzaToken();
        long istanteCreazioneInSecondi = token.getTimestampCreazione().getTimeInMillis()/1000;
        long istanteAttualeInSecondi = new GregorianCalendar().getTimeInMillis()/1000;
        return (istanteCreazioneInSecondi+secondiScadenza>istanteAttualeInSecondi);
    }

    public static boolean isRefreshTokenValid(GmailToken token){
        if (token==null){
            return false;
        }
        long secondiScadenza = token.getScadenzaRefreshToken();
        long istanteCreazioneInSecondi = token.getTimestampCreazione().getTimeInMillis()/1000;
        long istanteAttualeInSecondi = new GregorianCalendar().getTimeInMillis()/1000;
        return (istanteCreazioneInSecondi+secondiScadenza>istanteAttualeInSecondi);
    }
}
