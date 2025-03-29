package bnsshop.bnsshop;

import com.paypal.api.payments.*;
import com.paypal.core.rest.APIContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.ConfigPayPal;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
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

        Optional<APIContext> context = ConfigPayPal.getContext();
        if (context.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"Errore Durante il Pagamento",false);
            return;
        }

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

        GestioneServlet.inviaRisposta(response,200,"\""+approvalUrl+"\"",true);
    }
}
