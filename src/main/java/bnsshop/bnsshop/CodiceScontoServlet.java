package bnsshop.bnsshop;

import controllers.CodiceScontoController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CodiceSconto;
import org.json.JSONObject;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "CodiceScontoServlet", value = "/CodiceScontoServlet")
public class CodiceScontoServlet extends HttpServlet{
    CodiceScontoController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new CodiceScontoController();
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
        Optional<CodiceSconto> codiceSconto=null;
        List<CodiceSconto> codiciSconto=null;

        if (id!=-1){
            codiceSconto = this.controller.getObject(id);
        }else{
            codiciSconto = this.controller.getAllObjects();
        }

        if (codiceSconto!=null || codiciSconto!=null){
            if (codiceSconto!=null){
                GestioneServlet.inviaRisposta(response,200,codiceSconto.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,codiciSconto.toString(),true);
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
        int idCategoria = object.getInt("categoria");
        String codice = object.getString("codice");
        int valore = object.getInt("valore");
        String descrizione = object.getString("descrizione");
        String tipo = object.getString("tipo");
        String dataInizio= object.getString("data_inizio");
        String dataFine= object.getString("data_fine");
        int usoMassimo= object.getInt("uso_massimo");
        int usoPerUtente= object.getInt("uso_per_utente");
        int minimoAcquisto = object.getInt("minimo_acquisto");
        int attivo= object.getInt("attivo");

        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("codice",codice));
        request0.put(1, new RegisterServlet.RegisterFields("valore", String.valueOf(valore)));
        request0.put(2,new RegisterServlet.RegisterFields("descrizione",descrizione));
        request0.put(3,new RegisterServlet.RegisterFields("tipo",tipo));
        request0.put(4,new RegisterServlet.RegisterFields("data_inizio",dataInizio));
        request0.put(5,new RegisterServlet.RegisterFields("data_fine",dataFine));
        request0.put(6,new RegisterServlet.RegisterFields("uso_massimo",String.valueOf(usoMassimo)));
        request0.put(7,new RegisterServlet.RegisterFields("uso_per_utente",String.valueOf(usoPerUtente)));
        request0.put(8,new RegisterServlet.RegisterFields("minimo_acquisto",String.valueOf(minimoAcquisto)));
        request0.put(9,new RegisterServlet.RegisterFields("attivo",String.valueOf(attivo)));
        request0.put(10,new RegisterServlet.RegisterFields("categoria",String.valueOf(idCategoria)));
        if (controller.insertObject(request0)) {
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
        int id= Integer.parseInt((String) request.getParameter("id"));
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
        Map<Integer, RegisterServlet.RegisterFields> data = new HashMap<>();
        data.put(0,new RegisterServlet.RegisterFields("codice","" + object.getString("codice")));
        data.put(1,new RegisterServlet.RegisterFields("valore","" + object.getString("valore")));
        data.put(2,new RegisterServlet.RegisterFields("descrizione","" + object.getString("descrizione")));
        data.put(3,new RegisterServlet.RegisterFields("tipo","" + object.getString("tipo")));
        data.put(4,new RegisterServlet.RegisterFields("data_inizio","" + object.getString("data_inizio")));
        data.put(5,new RegisterServlet.RegisterFields("data_fine","" + object.getString("data_fine")));
        data.put(6,new RegisterServlet.RegisterFields("uso_massimo","" + object.getString("uso_massimo")));
        data.put(7,new RegisterServlet.RegisterFields("uso_per_utente","" + object.getString("uso_per_utente")));
        data.put(8,new RegisterServlet.RegisterFields("minimo_acquisto","" + object.getString("minimo_acquisto")));
        data.put(9,new RegisterServlet.RegisterFields("attivo","" + object.getString("attivo")));
        data.put(10,new RegisterServlet.RegisterFields("categoria","" + object.getString("categoria")));
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
        int id = Integer.parseInt((String) request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }
}