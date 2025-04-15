package bnsshop.bnsshop;

import controllers.Controllers;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import payPalManager.controllers.PaypalPaymentController;
import payPalManager.controllers.PaypalTokensController;
import payPalManager.models.Amount;
import payPalManager.models.PaypalPaymentRefunded;
import payPalManager.models.PaypalPaymentsCreated;
import payPalManager.models.PaypalTokens;
import payPalManager.utility.PaypalManagement;
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
        if(data.keySet().size() != 1 || !data.has("paymentId")){
            //se l'oggetto json è malformato o mancante di qualche campo vado in errore.
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }
        String paymentId;
        try{
            paymentId = data.getString("paymentId");
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
        List<PaypalPaymentsCreated> payments = paymentController.executeQuery("SELECT * FROM paypal_pagamento_creato WHERE payment_id='"+paymentId+"'");
        if(payments.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal\"",false);
            return;
        }
        String refundLink = payments.getFirst().getRefundLink().getHref();
        Amount amount = new Amount("EUR",payments.getFirst().getPaypalPayment().getNetAmount());

        //esecuzione del rimborso e invio risposta al client.
        Optional<PaypalPaymentRefunded> paymentRefunded = PaypalManagement.refundPayment(actualToken,refundLink,amount);
        if(paymentRefunded.isPresent()){
            System.out.println("Rimborso effettuato correttamente.");
            GestioneServlet.inviaRisposta(response,200,"\"Rimborso PayPal effettuato correttamente.\"",true);
        }else{
            System.err.println("Errore durante elaborazione del rimborso.");
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante elaborazione del rimborso PayPal.\"",false);
        }
    }
}
