package payPalManager.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import payPalManager.models.Amount;
import payPalManager.models.PaypalPaymentRefunded;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RefundPaymentRequest extends PaypalAPIRequest<PaypalPaymentRefunded>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String refundLink;
    private final Amount amount;

    private RefundPaymentRequest(Builder builder){
        super("",builder.accessToken);
        this.refundLink = builder.refundLink;
        this.amount = builder.amount;
    }

    @Override
    public String getEndpoint() {
        return refundLink;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Authorization","Bearer " + getAccessToken());
        headers.put("Prefer","return=representation");
        return headers;
    }

    @Override
    public String getBody() {
        JSONArray platformFees = new JSONArray();
        JSONObject platformFee = new JSONObject();
        platformFee.put("amount",amount.printJSONObject());
        platformFees.put(platformFee);
        JSONObject paymentInstruction = new JSONObject();
        paymentInstruction.put("platform_fees",platformFees);
        JSONObject data = new JSONObject();
        data.put("amount",amount.printJSONObject());
        data.put("payment_instruction",paymentInstruction);
        return data.toString(4);
    }

    @Override
    public Optional<PaypalPaymentRefunded> parseResponse(JSONObject jsonResponse) {

        //l'oggetto jsonResponse contiene i dati del pagamento rimborsato restituiti da paypal
        //inserire questi dati nel database.
        System.out.println(jsonResponse.toString(4));

        PaypalPaymentRefunded paymentRefunded = new PaypalPaymentRefunded();
        return Optional.of(paymentRefunded);
    }

    public static final class Builder{
        private String accessToken;
        private String refundLink;
        private Amount amount;

        public Builder(){
            accessToken = "";
            refundLink = "";
            amount = new Amount("",0.0);
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setRefundLink(String refundLink) {
            this.refundLink = refundLink;
            return this;
        }

        public Builder setAmount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public RefundPaymentRequest build(){
            return new RefundPaymentRequest(this);
        }
    }
}