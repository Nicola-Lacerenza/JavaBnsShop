package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.UtentiController;
import models.ProdottiFull;
import models.Utenti;
import payPalManager.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import utility.Database;
import utility.GestioneFileTesto;
import utility.GestioneServlet;

import java.io.*;
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


        String baseUrl = "https://api-m.sandbox.paypal.com";
        String returnUrl = "https://localhost:4200/checkout-success";
        String cancelUrl = "https://localhost:4200/checkout-failure";

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

        //PayPalTokenController specificController = (PayPalTokenController)controller ;
        List<PaypalToken> tokens = controller.getAllObjects();
        String actualToken;
        if (tokens.isEmpty() || !PayPalUtility.isTokenValid(tokens.getLast())){

            Optional<String> tmp = PayPalUtility.creaToken(controller,baseUrl);
            if (tmp.isPresent()){
                actualToken = tmp.get();
            }else{
                actualToken="";
            }
        }else{
            actualToken=tokens.getLast().getAccessToken();
        }

        Optional<OrdineCreato> ordineCreato = PayPalUtility.creaOrdine(idUtente,actualToken,baseUrl,cartItemsList,"EUR",prezzoTotale,"it-IT",returnUrl,cancelUrl);
        if (ordineCreato.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false);
        }else{
            System.out.println("Ordine con Id :" + ordineCreato.get().getOrderId() + " crato correttamente da PayPal!");
            List<LinkCreato> links = ordineCreato.get().getLinks();
            List<LinkCreato> linkFiltrato = links.stream().filter(link -> link.getRel().equals("payer-action")).toList();
            if (linkFiltrato.isEmpty()){
                GestioneServlet.inviaRisposta(response,500,"\"Errore durante la creazione dell'ordine PayPal\"",false);
                return;
            }
            GestioneServlet.inviaRisposta(response,200,"\""+linkFiltrato.getFirst().getHref()+"\"",true);
        }

    }

}
