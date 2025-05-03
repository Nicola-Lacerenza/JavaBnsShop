package bnsshop.bnsshop;

import controllers.ResiProdottiController;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ResiProdotti;
import models.Utenti;
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

    ResiProdottiController controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new ResiProdottiController();
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
        Optional<ResiProdotti> resoProdotto=Optional.empty();
        List<ResiProdotti> resiProdotti=null;

        if (id!=-1){
            resoProdotto = this.controller.getObject(id);
        }else{
            resiProdotti = this.controller.getAllObjects();
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
        int idOrdine = object.getInt("id_ordine");
        int idProdotto = object.getInt("id_prodotto");
        int idTaglia = object.getInt("id_taglia");
        int quantita = object.getInt("quantita");
        double prezzoUnitario = object.getDouble("prezzo_unitario");
        String motivo = object.getString("motivo");
        double importo = object.getDouble("importo");
        String valuta = object.getString("valuta");
        Map<Integer, QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("id_ordine",idOrdine, TipoVariabile.longNumber));
            request0.put(1,new QueryFields<>("id_utente",idUtente, TipoVariabile.longNumber));
            request0.put(2,new QueryFields<>("id_prodotto",idProdotto,TipoVariabile.longNumber));
            request0.put(3,new QueryFields<>("id_taglia",idTaglia,TipoVariabile.longNumber));
            request0.put(4,new QueryFields<>("quantita",quantita,TipoVariabile.longNumber));
            request0.put(5,new QueryFields<>("prezzo_unitario",prezzoUnitario,TipoVariabile.realNumber));
            request0.put(6,new QueryFields<>("motivo",motivo,TipoVariabile.string));
            request0.put(7,new QueryFields<>("importo",importo,TipoVariabile.realNumber));
            request0.put(8,new QueryFields<>("valuta",valuta,TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        int idReso = controller.insertObject(request0);
        if (idReso > 0) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(request,response,201,registrazione,true,false);
        }else{
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }

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
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

}
