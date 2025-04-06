package payPalManager.utility;


import payPalManager.models.PaypalOrdersCreated;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.PaypalTokens;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Optional;

public final class PaypalManagement{
    private PaypalManagement(){}

    public static boolean isTokenValid(PaypalTokens token){
        long expiresIn = token.getExpiresIn();
        String nonce = token.getNonce();
        String timestamp = nonce.substring(0,20);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.UK);
        Calendar now = new GregorianCalendar(Locale.UK);
        Calendar calendar = new GregorianCalendar();
        try{
            calendar.setTime(sdf.parse(timestamp));
        }catch(ParseException exception){
            exception.printStackTrace();
            return false;
        }
        long expirationMillis = expiresIn * 1000L;
        return now.getTimeInMillis() - calendar.getTimeInMillis() < expirationMillis;
    }

    public static Optional<PaypalTokens> createAPIToken(String apiBaseURL){
        PaypalAPIRequest<PaypalTokens> creationAPIToken = new CreateAPIToken(apiBaseURL);
        return creationAPIToken.execute();
    }

    public static Optional<PaypalOrdersCreated> createOrder(String accessToken, long userId, String apiBaseURL, String currency, double amount, String locale, String returnUrl, String cancelUrl){
        PaypalAPIRequest<PaypalOrdersCreated> creationOrder = new CreateOrderRequest.Builder()
                .setApiBaseURL(apiBaseURL)
                .setAccessToken(accessToken)
                .setUserId(userId)
                .setCurrency(currency)
                .setAmount(amount)
                .setLocale(locale)
                .setReturnUrl(returnUrl)
                .setCancelUrl(cancelUrl)
                .build();
        return creationOrder.execute();
    }

    public static Optional<PaypalPaymentsCreated> confirmPayment(String accessToken, String apiBaseURL, String orderId, String payerId){
        PaypalAPIRequest<PaypalPaymentsCreated> confirmPayment = new ConfirmPaymentRequest.Builder()
                .setApiBaseURL(apiBaseURL)
                .setAccessToken(accessToken)
                .setOrderId(orderId)
                .setPayerId(payerId)
                .build();
        return confirmPayment.execute();
    }
}