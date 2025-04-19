package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Utenti;
import org.json.JSONException;
import org.json.JSONObject;
import payPalManager.controllers.PaypalPaymentController;
import payPalManager.controllers.PaypalTokensController;
import payPalManager.models.Amount;
import payPalManager.models.PaypalPaymentRefunded;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.PaypalTokens;
import payPalManager.utility.PaypalManagement;
import utility.Database;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "RimborsiPaypalServlet", value = "/RimborsiPaypalServlet")
public class RimborsiPaypalServlet extends HttpServlet{
    private Controllers<PaypalTokens> tokenController;
    private Controllers<PaypalPaymentsCreated> paymentController;

    @Override
    public void init() throws ServletException {
        super.init();
        tokenController = new PaypalTokensController();
        paymentController = new PaypalPaymentController();
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

        int idUtente;
        boolean logged = GestioneServlet.isLogged(request,response);
        if (!logged) {
            idUtente=-1;
        }else{
            String email = GestioneServlet.validaToken(request,response);
            Controllers<Utenti> controllerUtenti = new UtentiController();
            List<Utenti> utenti = controllerUtenti.executeQuery("SELECT * FROM utenti WHERE email= '" + email + "'");
            if (utenti.isEmpty()){
                GestioneServlet.inviaRisposta(response,500,"\"Utente Non trovato!\"",false);
                return;
            }
            idUtente = utenti.getFirst().getId();
        }

        //lettura dati json provenienti dal client (paymentId cioè id del pagamento da rimborsare).
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
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }
        if(data.keySet().size() != 1 || !data.has("id")){
            //se l'oggetto json è malformato o mancante di qualche campo vado in errore.
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }
        int idOrdine;
        try{
            idOrdine = data.getInt("id");
        }catch(JSONException e){
            e.printStackTrace();
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }

        //variabili necessarie (url delle API di paypal)
        String baseUrl = "https://api-m.sandbox.paypal.com";

        //lettura del token per accedere alle API di paypal.
        List<PaypalTokens> tokens = tokenController.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !PaypalManagement.isTokenValid(tokens.getLast())){
            Optional<PaypalTokens> tmp = PaypalManagement.createAPIToken(baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get().getAccessToken();
            }else{
                GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
                return;
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }

        //estrazione del pagamento dal database, estrazione del link di rimborso e creazione importo da rimborsare.
        String query= "SELECT " +
                "paypal_pagamento_creato.id," +
                "paypal_pagamento_creato.id_ordine_paypal," +
                "paypal_pagamento_creato.payer.id," +
                "paypal_pagamento_creato.payment_id," +
                "paypal_pagamento_creato.status," +
                "paypal_pagamento_creato.paypal_fee," +
                "paypal_pagamento_creato.gross_amount," +
                "paypal_pagamento_creato.net_amount," +
                "paypal_pagamento_creato.refound_link_href," +
                "paypal_pagamento_creato.refound_link_request_method," +
                " FROM paypal_pagamento_creato JOIN ordine ON ordine.id = paypal_pagamento_creato.id_ordine_paypal WHERE ordine.id ='"+idOrdine+"'";
        List<PaypalPaymentsCreated> payments = paymentController.executeQuery(query);

        if(payments.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }
        String refundLink = payments.getFirst().getRefundLink().getHref();
        Amount amount = new Amount("EUR",payments.getFirst().getPaypalPayment().getNetAmount());

        //esecuzione del rimborso e invio risposta al client.
        Optional<PaypalPaymentRefunded> paymentRefunded = PaypalManagement.refundPayment(actualToken,refundLink,amount,idUtente,idOrdine);
        if(paymentRefunded.isPresent()){
            System.out.println("Rimborso effettuato correttamente.");
            GestioneServlet.inviaRisposta(response,200,"\"Rimborso PayPal effettuato correttamente.\"",true);
        }else{
            System.err.println("Errore durante elaborazione del rimborso.");
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal.\"",false);
        }
    }
}
