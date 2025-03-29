package bnsshop.bnsshop;

import com.paypal.core.rest.APIContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ConfigPayPal;
import utility.GestioneFileTesto;
import utility.GestioneServlet;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@WebServlet(name = "CreaPagamento", value = "/CreaPagamento")
public class CreaPagamentoServlet extends HttpServlet {
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length,Authorization");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*Optional<APIContext> context = ConfigPayPal.getContext();
        if (context.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"Errore Durante il Pagamento",false);
            return;
        }*/

        Map<String,String> paypalKeys = GestioneFileTesto.leggiFile("PaypalKeys.txt");

        BufferedReader reader=request.getReader();
        String row=reader.readLine();
        List<String> rows = new ArrayList<>();
        while (row!=null){
            rows.add(row);
            row=reader.readLine();
        }
        StringBuilder builder= new StringBuilder();
        for (String line:rows){
            builder.append(line);
        }
        String json=builder.toString();
        JSONArray arrayCart = new JSONArray(json);
        double prezzoTotale = 0;
        for (int i = 0;i < arrayCart.length(); i++){
            JSONObject elementoCarrello = (JSONObject) arrayCart.get(i);
            int quantity = elementoCarrello.getInt("quantity");
            JSONObject prodotto = elementoCarrello.getJSONObject("product");
            prezzoTotale +=(quantity*prodotto.getDouble("prezzo"));
        }
        /*Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(String.valueOf(prezzoTotale));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Ordine Corrente");
        List<Transaction> transactions = new LinkedList<>();
        transactions.add(transaction);
        Payer  payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://localhost:4200/checkout-failure");
        redirectUrls.setReturnUrl("https://localhost:8444/ConfermaPagamentoServlet");
        Payment createPayment = payment.create(context.get());
        String approvalUrl = "";

        for (Links link : createPayment.getLinks()){
            if(link.getRel().equals("approval_url")){
                approvalUrl = link.getHref();
            }
        }*/
        String baseUrl = "https://api-m.sandbox.paypal.com";
        String pathUrl = baseUrl + "/v2/oauth2/token";
        URL url = new URL(pathUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept","application/json");
        String auth = paypalKeys.get("ClientID") + ":" +paypalKeys.get("SecretID");
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        connection.setRequestProperty("Authorization","Basic "+encodedAuth);
        OutputStream os = connection.getOutputStream();
        os.write("grant_type=client_credentials".getBytes());
        os.flush();
        os.close();
        int responseCode = connection.getResponseCode();
        if (responseCode!=200){
            InputStream stream = connection.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuilder error = new StringBuilder();
            String line = buffer.readLine();
            while (line!=null){
                error.append(line);
                line = buffer.readLine();
            }
            System.out.println(error);
            GestioneServlet.inviaRisposta(response,responseCode,"Errore nel Pagamento!",false);
            return;
        }

        InputStream stream = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(isr);
        StringBuilder responseToken = new StringBuilder();
        String line = buffer.readLine();
        while (line!=null){
            responseToken.append(line);
            line = buffer.readLine();
        }

        String token = responseToken.toString();
        pathUrl = baseUrl + "/v2/checkout/orders";
        URL url = new URL(pathUrl);


        GestioneServlet.inviaRisposta(response,200,"\""+approvalUrl+"\"",true);
    }

    /*private Optional<String> effettuaRichiestaHttp(String pathUrl,String body){
        String baseUrl = "https://api-m.sandbox.paypal.com";
        URL url = new URL(pathUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept","application/json");
        if (pathUrl.equals("/v2/oauth2/token")){

        }
        OutputStream os = connection.getOutputStream();
        os.write(body.getBytes());
        os.flush();
        os.close();
        int responseCode = connection.getResponseCode();
        if (responseCode!=200){
            InputStream stream = connection.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuilder error = new StringBuilder();
            String line = buffer.readLine();
            while (line!=null){
                error.append(line);
                line = buffer.readLine();
            }
            System.out.println(error);
            GestioneServlet.inviaRisposta(response,responseCode,"Errore nel Pagamento!",false);
            return;
        }
    }*/

    private static class CartItem{
        private String name;
        private String description;
        private String currency;
        private String value;
        private String quantity;
        private String category;

        public CartItem(String name, String description, String currency, String value, String quantity, String category) {
            this.name = name;
            this.description = description;
            this.currency = currency;
            this.value = value;
            this.quantity = quantity;
            this.category = category;
        }

        @Override
        public String toString() {
            JSONObject output = new JSONObject();
            output.put("name",name);
            output.put("description",description);
            JSONObject unitAmount = new JSONObject();
            unitAmount.put("currency_code",currency);
            unitAmount.put("value",value);
            output.put("unit_amount",unitAmount);
            output.put("quantity",quantity);
            output.put("category",category);

        }
    }
}
