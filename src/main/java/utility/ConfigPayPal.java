package utility;

import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigPayPal {

    private ConfigPayPal(){}

    /*public static Optional<APIContext> getContext(){

        Map<String,String> paypalKeys = GestioneFileTesto.leggiFile("PaypalKeys.txt");

        String mode = "sandbox";
        Map<String,String> config = new HashMap<>();
        config.put("mode", mode);
        OAuthTokenCredential tokenCredential = new OAuthTokenCredential(paypalKeys.get("ClientID"),paypalKeys.get("SecretID"),config);
        APIContext context;
        try {
            context = new APIContext(tokenCredential.getAccessToken());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(context);
    }*/
}
