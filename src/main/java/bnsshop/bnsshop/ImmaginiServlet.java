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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ImmaginiServlet", value = "/ImmaginiServlet")
public class ImmaginiServlet extends HttpServlet{
    ImmaginiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ImmaginiController();
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
        Optional<Immagini> immagine=null;
        List<Immagini> immagini=null;

        if (id!=-1){
            immagine = this.controller.getObject(id);
        }else{
            immagini = this.controller.getAllObjects();
        }

        if (immagine!=null || immagini!=null){
            if (immagine!=null){
                GestioneServlet.inviaRisposta(response,200,immagine.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,immagini.toString(),true);
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
        if(!GestioneServlet.controllaRuolo(email)){
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
        String idProdotti= object.getString("id_prodotti");
        String url= object.getString("url");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_prodotti",idProdotti));
        request0.put(1,new RegisterServlet.RegisterFields("url",url));
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
        if(!GestioneServlet.controllaRuolo(email)){
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
        data.put(0,new RegisterServlet.RegisterFields("id_prodotti","" + object.getString("id_prodotti")));
        data.put(1,new RegisterServlet.RegisterFields("url","" + object.getString("url")));
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
        if(!GestioneServlet.controllaRuolo(email)){
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
        int id= Integer.parseInt((String) request.getAttribute("idimmagini"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}