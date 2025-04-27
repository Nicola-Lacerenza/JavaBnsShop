package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.ProdottiController;
import controllers.UtentiController;
import models.ProdottiFull;
import models.Utenti;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import payPalManager.CartItem;
import payPalManager.controllers.PaypalTokensController;
import payPalManager.models.LinksOrderCreated;
import payPalManager.models.PaypalOrdersCreated;
import payPalManager.models.PaypalTokens;
import payPalManager.utility.PaypalManagement;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "CreaPagamento", value = "/CreaPagamento")
public class CreaPagamentoServlet extends HttpServlet {
    private Controllers<PaypalTokens> controller;
    private Controllers<ProdottiFull> prodottiControllers;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new PaypalTokensController();
        prodottiControllers = new ProdottiController();
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

        int idUtente;
        boolean logged = GestioneServlet.isLogged(request);
        if (!logged) {
            idUtente=-1;
        }else{
            String email = GestioneServlet.validaToken(request,response);
            UtentiController controllerUtenti = new UtentiController();
            Optional<Utenti> utenti = controllerUtenti.getUserByEmail(email);
            if (utenti.isEmpty()){
                GestioneServlet.inviaRisposta(request,response,500,"\"Utente Non trovato!\"",false,false);
                return;
            }
            idUtente = utenti.get().getId();
        }

        String baseUrl = "https://api-m.sandbox.paypal.com";
        String returnUrl = "https://localhost:4200/checkout-success";
        String cancelUrl = "https://localhost:4200/checkout-failure";

        BufferedReader reader=request.getReader();
        String row=reader.readLine();
        List<String> rows = new LinkedList<>();
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
        List<CartItem> cartItemsList = new LinkedList<>();
        double prezzoTotale = 0;
        for (int i = 0;i < arrayCart.length(); i++){
            JSONObject elementoCarrello = (JSONObject) arrayCart.get(i);
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
            prezzoTotale +=(quantity*prodotto.getDouble("prezzo"));
            cartItemsList.add(new CartItem(prodottoFull,quantity,tagliaScelta));
        }

        for (CartItem cartItem : cartItemsList){
            if (!verificaProdotto(cartItem)){
                GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false,false);
                return;
            }
        }

        //PayPalTokenController specificController = (PayPalTokenController)controller ;
        List<PaypalTokens> tokens = controller.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !PaypalManagement.isTokenValid(tokens.getLast())){

            Optional<PaypalTokens> tmp = PaypalManagement.createAPIToken(baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get().getAccessToken();
            }else{
                GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false,false);
                return;
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }

        Optional<PaypalOrdersCreated> ordineCreato = PaypalManagement.createOrder(actualToken,idUtente,baseUrl,"EUR",prezzoTotale,"it-IT",returnUrl,cancelUrl,cartItemsList);
        if (ordineCreato.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false,false);
        }else{
            System.out.println("Ordine con Id :" + ordineCreato.get().getPaypalOrder().getOrderId() + " crato correttamente da PayPal!");
            List<LinksOrderCreated> links = ordineCreato.get().getLinks();
            List<LinksOrderCreated> linkFiltrato = links.stream().filter(link -> link.getRel().equals("payer-action")).toList();
            if (linkFiltrato.isEmpty()){
                GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false,false);
                return;
            }
            GestioneServlet.inviaRisposta(request,response,200,"\""+linkFiltrato.getFirst().getHref()+"\"",true,false);
        }

    }

    private boolean verificaProdotto(CartItem cartItem){
        Optional<ProdottiFull> controlloEsistenzaProdotto = prodottiControllers.getObject(cartItem.getProdottiFull().getId());
        if (controlloEsistenzaProdotto.isEmpty()){
            return false;
        }

        return true;
    }

}
