package bnsshop.bnsshop;

import controllers.ImmaginiProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ImmaginiProdotti;
import org.json.JSONObject;
import utility.GestioneServlet;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "ImmaginiProdottiServlet", value = "/ImmaginiProdottiServlet")
public class ImmaginiProdottiServlet extends HttpServlet{
    ImmaginiProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ImmaginiProdottiController();
    }

    // Gestione richiesta preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length,Authorization");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idImmagineProdotto;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                idImmagineProdotto = Integer.parseInt(idString);
            } else {
                idImmagineProdotto = -1;
            }
        }catch(NumberFormatException exception){
            idImmagineProdotto=-1;
        }
        Optional<ImmaginiProdotti> immagineProdotto=Optional.empty();
        List<ImmaginiProdotti> immagineProdotti=null;

        if (idImmagineProdotto!=-1){
            immagineProdotto = this.controller.getObject(idImmagineProdotto);
        }else{
            immagineProdotti = this.controller.getAllObjects();
        }

        if (immagineProdotto.isPresent() || immagineProdotti!=null){
            if (immagineProdotto.isPresent()){
                GestioneServlet.inviaRisposta(response,200,immagineProdotto.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,immagineProdotti.toString(),true);
            }
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
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
        JSONObject object = new JSONObject(json);
        int idImmagine= object.getInt("id_immagine");
        int idProdotto= object.getInt("id_prodotto");
        Map<Integer,QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("id_immagine",idImmagine,TipoVariabile.longNumber));
            request0.put(1,new QueryFields<>("id_prodotto",idProdotto,TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
            return;
        }
        int idImmagineProdotto = controller.insertObject(request0);
        if (idImmagineProdotto > 0) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(response,201,registrazione,true);
        }else{
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
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
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
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
        String json=builder.toString();
        JSONObject object = new JSONObject(json);
        Map<Integer,QueryFields<? extends Comparable<?>>> data = new HashMap<>();
        try{
            data.put(0,new QueryFields<>("id_immagine",object.getInt("id_immagine"),TipoVariabile.longNumber));
            data.put(1,new QueryFields<>("id_prodotto",object.getInt("id_prodotto"),TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
            return;
        }
        if (controller.updateObject(id,data)){
            String message="\"Product Updated Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
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
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
        int id= Integer.parseInt(request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}