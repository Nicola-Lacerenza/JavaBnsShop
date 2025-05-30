package bnsshop.bnsshop;

import controllers.ColoreProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ColoreProdotti;
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

@WebServlet(name = "ColoreProdottiServlet", value = "/ColoreProdottiServlet")
public class ColoreProdottiServlet extends HttpServlet{
    ColoreProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ColoreProdottiController();
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
        int idColoreModello;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                idColoreModello = Integer.parseInt(idString);
            } else {
                idColoreModello = -1;
            }
        }catch(NumberFormatException exception){
            idColoreModello=-1;
        }
        Optional<ColoreProdotti> coloreprodotto=Optional.empty();
        List<ColoreProdotti> coloriprodotti=null;

        if (idColoreModello!=-1){
            coloreprodotto = this.controller.getObject(idColoreModello);
        }else{
            coloriprodotti = this.controller.getAllObjects();
        }

        if (coloreprodotto.isPresent() || coloriprodotti!=null){
            if (coloreprodotto.isPresent()){
                GestioneServlet.inviaRisposta(request,response,200,coloreprodotto.get().toString(),true,false);
            }else{
                GestioneServlet.inviaRisposta(request,response,200,coloriprodotti.toString(),true,false);
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
        int idColore= object.getInt("id_colore");
        int idProdotto= object.getInt("id_prodotto");
        Map<Integer, QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("id_colore",idColore, TipoVariabile.longNumber));
            request0.put(1,new QueryFields<>("id_prodotto",idProdotto,TipoVariabile.longNumber));
        }catch (SQLException exception){
            exception.printStackTrace();
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        int idColoreProdotto = controller.insertObject(request0);
        if (idColoreProdotto > 0) {
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
        int idCategoria= Integer.parseInt(request.getParameter("id"));
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
            data.put(0,new QueryFields<>("id_colore",object.getInt("id_colore"),TipoVariabile.longNumber));
            data.put(1,new QueryFields<>("id_prodotto",object.getInt("id_prodotto"),TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        if (controller.updateObject(idCategoria,data)){
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
        int id= Integer.parseInt(request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

}