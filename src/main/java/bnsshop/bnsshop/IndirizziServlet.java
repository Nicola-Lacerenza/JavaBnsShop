package bnsshop.bnsshop;

import controllers.IndirizziController;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Indirizzi;
import models.Utenti;
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

@WebServlet(name = "IndirizziServlet", value = "/IndirizziServlet")
public class IndirizziServlet extends HttpServlet {
    IndirizziController controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new IndirizziController("");
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

        boolean logged = GestioneServlet.isLogged(request);
        if (!logged) {
            GestioneServlet.inviaRisposta(response,200,"[]",true);
            return;
        }
        String email = GestioneServlet.validaToken(request,response);
        controller = new IndirizziController(email);
        List<Indirizzi> indirizzi = controller.getAllObjects();
        GestioneServlet.inviaRisposta(response,200,indirizzi.toString(),true);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

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
        boolean isLogged = GestioneServlet.isLogged(request);
        String id_utente;
        if (!isLogged){
            id_utente = "";
        }else{
            UtentiController controllerUtenti = new UtentiController();
            String email = GestioneServlet.extractEmail(request);
            Optional<Utenti> user = controllerUtenti.getUserByEmail(email);
            if (user.isEmpty()){
                String message = "\"Errore durante la registrazione.\"";
                GestioneServlet.inviaRisposta(response,500,message,false);
                return;
            }
            id_utente = String.valueOf(user.get().getId());
        }

        String nome= object.getString("nome");
        String cognome= object.getString("cognome");
        String citta= object.getString("citta");
        String stato= object.getString("stato");
        String cap= object.getString("cap");
        String indirizzo= object.getString("indirizzo");
        String email1 = object.getString("email");
        String numero_telefono= object.getString("numero_telefono");
        Map<Integer,QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("id_utente",id_utente,TipoVariabile.string));
            request0.put(1,new QueryFields<>("nome",nome,TipoVariabile.string));
            request0.put(2,new QueryFields<>("cognome",cognome,TipoVariabile.string));
            request0.put(3,new QueryFields<>("citta",citta,TipoVariabile.string));
            request0.put(4,new QueryFields<>("stato",stato,TipoVariabile.string));
            request0.put(5,new QueryFields<>("cap",cap,TipoVariabile.string));
            request0.put(6,new QueryFields<>("indirizzo",indirizzo,TipoVariabile.string));
            request0.put(7,new QueryFields<>("email",email1,TipoVariabile.string));
            request0.put(8,new QueryFields<>("numero_telefono",numero_telefono,TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
            return;
        }

        int idIndirizzo = controller.insertObject(request0);
        if (idIndirizzo > 0) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(response,201,registrazione,true);
        }else{
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{

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
            data.put(0,new QueryFields<>("nome",object.getString("nome"),TipoVariabile.string));
            data.put(1,new QueryFields<>("cognome",object.getString("cognome"),TipoVariabile.string));
            data.put(2,new QueryFields<>("citta",object.getString("citta"),TipoVariabile.string));
            data.put(3,new QueryFields<>("stato",object.getString("stato"),TipoVariabile.string));
            data.put(4,new QueryFields<>("cap",object.getString("cap"),TipoVariabile.string));
            data.put(5,new QueryFields<>("indirizzo",object.getString("indirizzo"),TipoVariabile.string));
            data.put(6,new QueryFields<>("email",object.getString("email"),TipoVariabile.string));
            data.put(7,new QueryFields<>("numero_telefono",object.getString("numero_telefono"),TipoVariabile.string));
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
        int id = Integer.parseInt(request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}
