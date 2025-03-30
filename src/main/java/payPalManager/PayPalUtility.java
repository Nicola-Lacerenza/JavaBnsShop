package payPalManager;

import bnsshop.bnsshop.RegisterServlet;
import controllers.Controllers;
import models.Ordine;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.Database;
import utility.GestioneFileTesto;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayPalUtility {

    private PayPalUtility(){}

    public static boolean isTokenValid(PaypalToken token){

        int expiresIn = token.getExpiresIn();
        String nonce = token.getNonce();
        String timestamp = nonce.substring(0,20);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Calendar now = new GregorianCalendar(Locale.UK);
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(sdf.parse(timestamp));

        }catch (ParseException e){
            e.printStackTrace();
            return false;
        }
        if (now.getTimeInMillis() - calendar.getTimeInMillis()<(expiresIn*1000)){
            return true;
        }
        return false;
    }

    public static Optional<String> creaToken(Controllers<PaypalToken> controller, String baseUrl) throws IOException {

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

    public static Optional<OrdineCreato> creaOrdine(int idUtente, String accessToken, String apiBaseURL, List<CartItem> cartItems, String currency, double prezzoTotale, String locale, String returnUrl, String cancelUrl) {
        URL url;
        try {
            url = URI.create(apiBaseURL + "/v2/checkout/orders").toURL();
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }

        connection.setDoOutput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        JSONObject orderData = new JSONObject();
        orderData.put("intent", "CAPTURE");

        JSONArray purchaseUnits = new JSONArray();
        JSONObject purchaseUnit = new JSONObject();

        // Creazione dell'array degli items
        JSONArray items = new JSONArray();
        for (CartItem cartItem : cartItems) {
            JSONObject item = new JSONObject();
            item.put("name", cartItem.getProdottiFull().getNomeModello() + " (" + cartItem.getTagliaScelta() + ")");
            item.put("description", cartItem.getProdottiFull().getDescrizioneModello());

            // Creazione dell'oggetto unit_amount con il prezzo formattato
            JSONObject unitAmount = new JSONObject();
            unitAmount.put("currency_code", currency);
            unitAmount.put("value", String.format(Locale.US, "%.2f", cartItem.getProdottiFull().getPrezzo()));
            item.put("unit_amount", unitAmount);

            item.put("quantity", String.valueOf(cartItem.getQuantity()));
            item.put("category", "PHYSICAL_GOODS");
            // item.put("image_url", cartItem.getProdottiFull().getUrl().getFirst());

            items.put(item);
        }
        purchaseUnit.put("items", items);

        // Creazione dell'oggetto amountData con breakdown
        JSONObject amountData = new JSONObject();
        amountData.put("currency_code", currency);
        amountData.put("value", String.format(Locale.US, "%.2f", prezzoTotale));

        JSONObject itemTotal = new JSONObject();
        itemTotal.put("currency_code", currency);
        itemTotal.put("value", String.format(Locale.US, "%.2f", prezzoTotale));

        JSONObject breakdown = new JSONObject();
        breakdown.put("item_total", itemTotal);

        amountData.put("breakdown", breakdown);
        purchaseUnit.put("amount", amountData);
        purchaseUnits.put(purchaseUnit);
        orderData.put("purchase_units", purchaseUnits);

        // Configurazione del payment source
        JSONObject paymentSource = new JSONObject();
        JSONObject paypal = new JSONObject();
        JSONObject experienceContext = new JSONObject();
        experienceContext.put("payment_method_preference", "IMMEDIATE_PAYMENT_REQUIRED");
        experienceContext.put("shipping_preference", "GET_FROM_FILE");
        experienceContext.put("locale", locale);
        experienceContext.put("return_url", returnUrl);
        experienceContext.put("cancel_url", cancelUrl);
        experienceContext.put("brand_name", "Isabella Quacquarelli");
        experienceContext.put("landing_page", "LOGIN");
        experienceContext.put("user_action", "PAY_NOW");
        paypal.put("experience_context", experienceContext);
        paymentSource.put("paypal", paypal);
        orderData.put("payment_source", paymentSource);

        // Invio della richiesta e lettura della risposta
        StringBuilder response;
        int responseCode;
        try {
            OutputStream out = connection.getOutputStream();
            out.write(orderData.toString(4).getBytes());
            out.flush();
            out.close();
            responseCode = connection.getResponseCode();
            InputStream stream;
            if (responseCode != HttpURLConnection.HTTP_OK) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader in = new BufferedReader(isr);
            response = new StringBuilder();
            String line = in.readLine();
            while (line != null) {
                response.append(line);
                line = in.readLine();
            }
            in.close();
            isr.close();
            stream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }

        String bodyResponse = response.toString();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.err.println(bodyResponse);
            return Optional.empty();
        }

        JSONObject jsonResponse = new JSONObject(bodyResponse);
        String orderId = jsonResponse.getString("id");
        String status = jsonResponse.getString("status");
        List<LinkCreato> list = new LinkedList<>();
        JSONArray links = jsonResponse.getJSONArray("links");
        for (int i = 0; i < links.length(); i++) {
            JSONObject link = links.getJSONObject(i);
            String method = link.getString("method");
            String rel = link.getString("rel");
            String href = link.getString("href");
            list.add(new LinkCreato(method, rel, href));
        }

        // Inserimento nel database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC non trovato", e);
        }
        int idOrdine = -1;

        Connection connectionDB = null;
        boolean output = false;
        try {
            connectionDB = DriverManager.getConnection(Database.getDatabaseUrl(), Database.getDatabaseUsername(), Database.getDatabasePassword());
            connectionDB.setAutoCommit(false); // Avvia transazione

            // Correggi la query SQL rimuovendo la virgola in eccesso
            String query1 = "INSERT INTO ordine (id_utente, id_pagamento, id_indirizzo, stato_ordine, importo, valuta, locale_utente) " +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connectionDB.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setInt(1, idUtente);
            preparedStatement1.setInt(2, 1);
            preparedStatement1.setInt(3, 1);
            preparedStatement1.setString(4, status);
            preparedStatement1.setDouble(5, prezzoTotale);
            preparedStatement1.setString(6, currency);
            preparedStatement1.setString(7, locale);
            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            if (generatedKeys.next()) {
                idOrdine = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Errore: Nessun ID ordine generato.");
            }
            connectionDB.commit();
            output = true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (connectionDB != null) {
                try {
                    connectionDB.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            output = false;
        } finally {
            if (connectionDB != null) {
                try {
                    connectionDB.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        Ordine order = new Ordine(idOrdine,idUtente,1,1,status,new GregorianCalendar(),new GregorianCalendar(),prezzoTotale,currency,locale);
        OrdineCreato output1 = new OrdineCreato(list, orderId, order);
        return Optional.of(output1);
    }

}
