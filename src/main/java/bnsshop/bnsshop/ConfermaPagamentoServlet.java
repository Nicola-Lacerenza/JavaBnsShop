package bnsshop.bnsshop;

import controllers.Controllers;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import payPalManager.controllers.PaypalTokensController;
import payPalManager.models.LinksOrderCreated;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.PaypalTokens;
import payPalManager.utility.PaypalManagement;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ConfermaPagamentoServlet", value = "/ConfermaPagamentoServlet")

public class ConfermaPagamentoServlet extends HttpServlet {
    Controllers<PaypalTokens> controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new PaypalTokensController();
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
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante la conferma del pagamento PayPal\"",false);
            return;
        }

        //variabili che servono per confermare il pagamento.
        String orderId;
        String payerId;
        try {
            orderId = data.getString("token"); //l'id dell'ordine corrisponde al token
            payerId = data.getString("payerId"); //estrazione dall'oggetto json
        }catch(JSONException e){
            e.printStackTrace();
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante la conferma del pagamento PayPal\"",false);
            return;
        }
        String baseUrl = "https://api-m.sandbox.paypal.com"; //url delle api di paypal

        //lettura del token per accedere alle API di paypal.
        List<PaypalTokens> tokens = controller.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !PaypalManagement.isTokenValid(tokens.getLast())){
            Optional<PaypalTokens> tmp = PaypalManagement.createAPIToken(baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get().getAccessToken();
            }else{
                GestioneServlet.inviaRisposta(response,500,"\"Errore durante la conferma del pagamento PayPal\"",false);
                return;
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }

        //esecuzione della conferma del pagamento e invio della risposta al client.
        Optional<PaypalPaymentsCreated> pagamentoCreato = PaypalManagement.confirmPayment(actualToken,baseUrl,orderId,payerId);
        if(pagamentoCreato.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante la conferma del pagamento PayPal\"",false);
        }else{
            LinksOrderCreated refundLink = pagamentoCreato.get().getRefundLink();

            //Implementare in questo punto il salvataggio su file del link di rimborso (variabile refundLink qui sopra).
            //In caso di rimborso effettuare una richiesta a quel link con i dati necessari (importo da rimborsare,ecc...).
            //Per l'importo da rimborsare scegliere se rimborsare quello netto ricevuto o meno (importo totale - commissioni paypal).
            System.out.println("LINK DI RIMBORSO: " + refundLink);

            GestioneServlet.inviaRisposta(response,200,"\"Pagamento andato a buon fine.\"",true);
        }
    }
}
