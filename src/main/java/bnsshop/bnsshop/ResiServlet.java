package bnsshop.bnsshop;

import controllers.ResiController;
import controllers.ResiProdottiController;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Resi;
import models.ResiProdotti;
import models.Utenti;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.GestioneServlet;
import utility.QueryFields;
import utility.TipoVariabile;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "ResiServlet", value = "/ResiServlet")
public class ResiServlet extends HttpServlet {

    ResiProdottiController resiProdottiController;
    ResiController resiController;

    @Override
    public void init() throws ServletException {
        super.init();
        resiProdottiController = new ResiProdottiController();
        resiController = new ResiController();
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int id;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                id = Integer.parseInt(idString);
            } else {
                id = -1;
            }
        }catch(NumberFormatException exception){
            id=-1;
        }
        Optional<Resi> resoProdotto=Optional.empty();
        List<Resi> resiProdotti=null;

        if (id!=-1){
            resoProdotto = this.resiController.getObject(id);
        }else{
            resiProdotti = this.resiController.getAllObjects();
        }

        if (resoProdotto.isPresent() || resiProdotti!=null){
            if (resoProdotto.isPresent()){
                GestioneServlet.inviaRisposta(request,response,200,resoProdotto.get().toString(),true,false);
            }else{
                GestioneServlet.inviaRisposta(request,response,200,resiProdotti.toString(),true,false);
            }
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String email = GestioneServlet.validaToken(request, response);
        if (email == null) {
            return;
        }

        UtentiController controllerUtenti = new UtentiController();
        Optional<Utenti> utenti = controllerUtenti.getUserByEmail(email);
        if (utenti.isEmpty()) {
            GestioneServlet.inviaRisposta(request,response, 500, "{\"error\":\"Utente non trovato!\"}", false,false);
            return;
        }
        int idUtente = utenti.get().getId();

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

        JSONObject object = new JSONObject(builder.toString());
        JSONArray jsonArray = object.getJSONArray("body");
        List<Map<Integer, QueryFields<? extends Comparable<?>>> > request0 = new LinkedList<>();
        for (int i = 0;i<jsonArray.length();i++){
            JSONObject resoProdotto = jsonArray.getJSONObject(i);
            JSONObject ordine = resoProdotto.getJSONObject("ordine");
            int idOrdine = ordine.getInt("id");
            int idProdotto = resoProdotto.getInt("id_prodotto");
            String numeroTaglia = resoProdotto.getString("numero_taglia");
            String motivo = resoProdotto.getString("motivo");
            double prezzoUnitario = resoProdotto.getDouble("prezzo_unitario");
            int quantita = resoProdotto.getInt("quantita");
            String valuta = resoProdotto.getString("valuta");
            Map<Integer, QueryFields<? extends Comparable<?>>> mappaSingoloReso = new HashMap<>();
            try{
                mappaSingoloReso.put(0,new QueryFields<>("id_ordine",idOrdine, TipoVariabile.longNumber));
                mappaSingoloReso.put(1,new QueryFields<>("id_utente",idUtente, TipoVariabile.longNumber));
                mappaSingoloReso.put(2,new QueryFields<>("id_prodotto",idProdotto,TipoVariabile.longNumber));
                mappaSingoloReso.put(3,new QueryFields<>("numero_taglia",numeroTaglia,TipoVariabile.string));
                mappaSingoloReso.put(4,new QueryFields<>("quantita",quantita,TipoVariabile.longNumber));
                mappaSingoloReso.put(5,new QueryFields<>("prezzo_unitario",prezzoUnitario,TipoVariabile.realNumber));
                mappaSingoloReso.put(6,new QueryFields<>("motivo",motivo,TipoVariabile.string));
                mappaSingoloReso.put(7,new QueryFields<>("valuta",valuta,TipoVariabile.string));
            }catch(SQLException exception){
                exception.printStackTrace();
                String message = "\"Errore durante la registrazione.\"";
                GestioneServlet.inviaRisposta(request,response,500,message,false,false);
                return;
            }
            request0.add(mappaSingoloReso);
        }

        int idReso = resiProdottiController.insertObject(request0);
        if (idReso > 0) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(request,response,201,registrazione,true,false);
        }else{
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
        GestioneServlet.inviaRisposta(request,response,500,"\"message\"",false,false);

    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
            return;
        }
        int id= Integer.parseInt(request.getParameter("id"));
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

    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
            return;
        }
        int id = Integer.parseInt((String) request.getAttribute("id"));
        if (this.resiProdottiController.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

}
