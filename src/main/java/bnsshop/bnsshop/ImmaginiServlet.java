package bnsshop.bnsshop;

import controllers.ImmaginiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Immagini;
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

@WebServlet(name = "ImmaginiServlet", value = "/ImmaginiServlet")
public class ImmaginiServlet extends HttpServlet{
    ImmaginiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ImmaginiController();
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
            }        }catch(NumberFormatException exception){
            id=-1;
        }
        Optional<Immagini> immagine=Optional.empty();
        List<Immagini> immagini=null;

        if (id!=-1){
            immagine = this.controller.getObject(id);
        }else{
            immagini = this.controller.getAllObjects();
        }

        if (immagine.isPresent() || immagini!=null){
            if (immagine.isPresent()){
                GestioneServlet.inviaRisposta(request,response,200,immagine.get().toString(),true,false);
            }else{
                GestioneServlet.inviaRisposta(request,response,200,immagini.toString(),true,false);
            }
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
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
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
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
        String url= object.getString("url");
        Map<Integer, QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("url",url, TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        int idImmagine = controller.insertObject(request0);
        if (idImmagine > 0) {
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
        String json=builder.toString();
        JSONObject object = new JSONObject(json);
        Map<Integer, QueryFields<? extends Comparable<?>>> data = new HashMap<>();
        try{
            data.put(0,new QueryFields<>("url",object.getString("url"),TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        if (controller.updateObject(id,data)){
            String message="\"Product Updated Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
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
        int id= Integer.parseInt((String) request.getAttribute("idimmagini"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

}