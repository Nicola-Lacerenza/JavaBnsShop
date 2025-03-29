package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.PayPalTokenController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Pagamenti;
import models.PaypalToken;
import org.json.JSONArray;
import org.json.JSONObject;

import utility.GestioneFileTesto;
import utility.GestioneServlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

@WebServlet(name = "CreaPagamento", value = "/CreaPagamento")
public class CreaPagamentoServlet extends HttpServlet {

    private Controllers<PaypalToken> controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new PayPalTokenController();
    }

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

        String baseUrl = "https://api-m.sandbox.paypal.com";

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
        JSONObject tmp1 = new JSONObject(json);
        JSONArray arrayCart = tmp1.getJSONArray("body");
        double prezzoTotale = 0;
        for (int i = 0;i < arrayCart.length(); i++){
            JSONObject elementoCarrello = (JSONObject) arrayCart.get(i);
            int quantity = elementoCarrello.getInt("quantity");
            JSONObject prodotto = elementoCarrello.getJSONObject("product");
            prezzoTotale +=(quantity*prodotto.getDouble("prezzo"));
        }

        PayPalTokenController specificController = (PayPalTokenController)controller ;
        List<PaypalToken> tokens = specificController.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !specificController.isTokenValid(tokens.getLast())){

            Optional<String> tmp = creaToken(baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get();
            }else{
                actualToken="";
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }


        System.out.println(tokens);
        System.out.println(actualToken);

        /*String token = responseToken.toString();
        pathUrl = baseUrl + "/v2/checkout/orders";
        URL url = new URL(pathUrl);*/


        GestioneServlet.inviaRisposta(response,501,"\"Metodo non implementato\"",false);
    }

    private Optional<String> creaToken(String baseUrl) throws IOException{

        Map<String,String> paypalKeys = GestioneFileTesto.leggiFile("PaypalKeys.txt");


        String pathUrl = baseUrl + "/v1/oauth2/token";
        URL url = URI.create(pathUrl).toURL();
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
            InputStream stream = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuilder error = new StringBuilder();
            String line = buffer.readLine();
            while (line!=null){
                error.append(line);
                line = buffer.readLine();
            }
            System.out.println(error);
            return Optional.empty();
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

        String json = responseToken.toString();
        JSONObject object = new JSONObject(json);

        Map<Integer, RegisterServlet.RegisterFields> request0 = new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("access_token",object.getString("access_token")));
        request0.put(1,new RegisterServlet.RegisterFields("scope",object.getString("scope")));
        request0.put(2,new RegisterServlet.RegisterFields("token_type",object.getString("token_type")));
        request0.put(3,new RegisterServlet.RegisterFields("app_id",object.getString("app_id")));
        request0.put(4,new RegisterServlet.RegisterFields("expires_in",""+object.getInt("expires_in")));
        request0.put(5,new RegisterServlet.RegisterFields("nonce",object.getString("nonce")));

        if (!controller.insertObject(request0)){
            System.out.println("Token non memorizzato nel Database");
        }
        return Optional.of(object.getString("access_token"));
    }

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
            return output.toString(4);
        }
    }
}
