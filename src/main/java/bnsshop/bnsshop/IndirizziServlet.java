package bnsshop.bnsshop;

import controllers.BrandController;
import controllers.IndirizziController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Fornitori;
import models.Indirizzi;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "IndirizziServlet", value = "/IndirizziServlet")
public class IndirizziServlet extends HttpServlet {
    IndirizziController controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new IndirizziController();
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
        Optional<Indirizzi> indirizzo=null;
        List<Indirizzi> indirizzi=null;

        if (id!=-1){
            indirizzo = this.controller.getObject(id);
        }else{
            indirizzi = this.controller.getAllObjects();
        }

        if (indirizzo!=null || indirizzi!=null){
            if (indirizzo!=null){
                GestioneServlet.inviaRisposta(response,200,indirizzo.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,indirizzi.toString(),true);
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
        JSONObject object = new JSONObject(json);
        String id_utente= object.getString("id_utente");
        String nome= object.getString("nome");
        String cognome= object.getString("cognome");
        String citta= object.getString("citta");
        String stato= object.getString("stato");
        String cap= object.getString("cap");
        String indirizzo= object.getString("indirizzo");
        String email1 = object.getString("email");
        String numero_telefono= object.getString("numero_telefono");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_utente",id_utente));
        request0.put(1,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(2,new RegisterServlet.RegisterFields("cognome",cognome));
        request0.put(3,new RegisterServlet.RegisterFields("citta",citta));
        request0.put(4,new RegisterServlet.RegisterFields("stato",stato));
        request0.put(5,new RegisterServlet.RegisterFields("cap",cap));
        request0.put(6,new RegisterServlet.RegisterFields("indirizzo",indirizzo));
        request0.put(7,new RegisterServlet.RegisterFields("email",email1));
        request0.put(8,new RegisterServlet.RegisterFields("numero_telefono",numero_telefono));
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
        JSONObject object = new JSONObject(json);
        Map<Integer, RegisterServlet.RegisterFields> data = new HashMap<>();
        data.put(0,new RegisterServlet.RegisterFields("nome","" + object.getString("nome")));
        data.put(1,new RegisterServlet.RegisterFields("cognome","" + object.getString("cognome")));
        data.put(2,new RegisterServlet.RegisterFields("citta","" + object.getString("citta")));
        data.put(3,new RegisterServlet.RegisterFields("stato","" + object.getString("stato")));
        data.put(4,new RegisterServlet.RegisterFields("cap","" + object.getString("cap")));
        data.put(5,new RegisterServlet.RegisterFields("indirizzo","" + object.getString("indirizzo")));
        data.put(6,new RegisterServlet.RegisterFields("email","" + object.getString("email")));
        data.put(7,new RegisterServlet.RegisterFields("numero_telefono","" + object.getString("numero_telefono")));
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
