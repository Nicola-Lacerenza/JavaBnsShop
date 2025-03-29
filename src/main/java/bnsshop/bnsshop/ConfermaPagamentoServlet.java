package bnsshop.bnsshop;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.ConfigPayPal;
import utility.GestioneFileTesto;
import utility.GestioneServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "ConfermaPagamentoServlet", value = "/ConfermaPagamentoServlet")

public class ConfermaPagamentoServlet extends HttpServlet {

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

        JSONObject risposta = new JSONObject();

        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("payerId");
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executePayment = payment.execute(context, paymentExecution);
            risposta.put("paymentId", executePayment.getId());
            risposta.put("success", "Pagamento Completato con Successo");
            GestioneServlet.inviaRisposta(response, 200, risposta.toString(4), true);
        }catch (PayPalRESTException e){

            e.printStackTrace();
            risposta.put("error","Pagamento non Completato");
            GestioneServlet.inviaRisposta(response,500,risposta.toString(4),false);

        }
    }
}
