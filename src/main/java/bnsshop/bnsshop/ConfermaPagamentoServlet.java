package bnsshop.bnsshop;

import controllers.Controllers;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ProdottiFull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import payPalManager.CartItem;
import payPalManager.controllers.PaypalTokensController;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.PaypalTokens;
import payPalManager.utility.PaypalManagement;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ConfermaPagamentoServlet", value = "/ConfermaPagamentoServlet")
public class ConfermaPagamentoServlet extends HttpServlet {
    private Controllers<PaypalTokens> controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new PaypalTokensController();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        if(GestioneServlet.aggiungiCorsSicurezzaHeadersDynamicPage(request,response)){
            System.out.println("CORS and security headers added correctly in the response.");
        }else{
            System.err.println("Error writing the CORS and security headers in the response.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //lettura dati json provenienti dal client (token e payerId).
        BufferedReader in = request.getReader();
        StringBuilder lines = new StringBuilder();
        String line = in.readLine();
        while(line != null){
            lines.append(line);
            line = in.readLine();
        }
        String json = lines.toString();
        JSONObject data;
        try{
            data = new JSONObject(json);
        }catch(JSONException e){
            e.printStackTrace();
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la conferma del pagamento PayPal\"",false,false);
            return;
        }
        if(data.keySet().size() != 3 || !data.has("token") || !data.has("payerId") || !data.has("cartItems")){
            //se l'oggetto json è malformato o mancante di qualche campo vado in errore.
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la conferma del pagamento PayPal\"",false,false);
            return;
        }

        //variabili che servono per confermare il pagamento.
        JSONArray cartItemsJson;
        String orderId;
        String payerId;
        try {
            cartItemsJson = data.getJSONArray("cartItems");
            orderId = data.getString("token"); //l'id dell'ordine corrisponde al token
            payerId = data.getString("payerId"); //estrazione dall'oggetto json
        }catch(JSONException e){
            e.printStackTrace();
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la conferma del pagamento PayPal\"",false,false);
            return;
        }
        String baseUrl = "https://api-m.sandbox.paypal.com"; //url delle api di paypal

        List<CartItem> cartItems = new LinkedList<>();
        for (int i = 0;i < cartItemsJson.length(); i++){
            JSONObject elementoCarrello = (JSONObject) cartItemsJson.get(i);
            JSONObject tmp = elementoCarrello.getJSONObject("product");
            int idProdotto = tmp.getInt("id");
            int idModello = tmp.getInt("id_modello");
            String nomeModello = tmp.getString("nome_modello");
            String descrizioneModello = tmp.getString("descrizione_modello");
            int idCategoria = tmp.getInt("id_categoria");
            String nomeCategoria = tmp.getString("nome_categoria");
            String target = tmp.getString("target");
            int idBrand = tmp.getInt("id_brand");
            String nomeBrand = tmp.getString("nome_brand");
            String descrizioneBrand = tmp.getString("descrizione_brand");
            int statoPubblicazione = tmp.getInt("stato_pubblicazione");
            int prezzo = tmp.getInt("prezzo");
            List<String> urls = new LinkedList<>();
            JSONArray urlEstratti = tmp.getJSONArray("url");
            for (int j=0;j<urlEstratti.length();j++){
                urls.add(urlEstratti.getString(j));
            }
            List<String> colori = new LinkedList<>();
            JSONArray coloriEstratti = tmp.getJSONArray("nome_colore");
            for (int j=0;j<coloriEstratti.length();j++){
                colori.add(coloriEstratti.getString(j));
            }

            ProdottiFull prodottoFull = new ProdottiFull(idProdotto,idModello,nomeModello,descrizioneModello,idCategoria,nomeCategoria,target,idBrand,nomeBrand,descrizioneBrand,statoPubblicazione,prezzo,new LinkedList<>(),urls,colori);
            int quantity = elementoCarrello.getInt("quantity");
            String tagliaScelta = elementoCarrello.getString("tagliaScelta");
            JSONObject prodotto = elementoCarrello.getJSONObject("product");
            cartItems.add(new CartItem(prodottoFull,quantity,tagliaScelta));
        }


        //lettura del token per accedere alle API di paypal.
        List<PaypalTokens> tokens = controller.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !PaypalManagement.isTokenValid(tokens.getLast())){
            Optional<PaypalTokens> tmp = PaypalManagement.createAPIToken(baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get().getAccessToken();
            }else{
                GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la conferma del pagamento PayPal\"",false,false);
                return;
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }

        //esecuzione della conferma del pagamento e invio della risposta al client.
        Optional<PaypalPaymentsCreated> pagamentoCreato = PaypalManagement.confirmPayment(actualToken,baseUrl,orderId,payerId,cartItems);
        if(pagamentoCreato.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la conferma del pagamento PayPal\"",false,false);
        }else{
            System.out.println("Pagamento \"" + pagamentoCreato.get().getPaypalPayment().getPaymentId() + "\" confermato correttamente.");
            GestioneServlet.inviaRisposta(request,response,200,"\"Pagamento andato a buon fine.\"",true,false);
        }
    }
}
