package bnsshop.bnsshop;

import controllers.TagliaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Taglia;
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

@WebServlet(name = "TagliaServlet", value = "/TagliaServlet")
public class TagliaServlet extends HttpServlet{
    TagliaController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new TagliaController();
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
        int idTaglia;
        try{
            idTaglia = Integer.parseInt((String) request.getAttribute("idtaglia"));
        }catch(NumberFormatException exception){
            idTaglia=-1;
        }
        Optional<Taglia> taglia=Optional.empty();
        List<Taglia> taglie=null;

        if (idTaglia!=-1){
            taglia = this.controller.getObject(idTaglia);
        }else{
            taglie = this.controller.getAllObjects();
        }

        if (taglia.isPresent() || taglie!=null){
            if (taglia.isPresent()){
                GestioneServlet.inviaRisposta(request,response,200,taglia.get().toString(),true,false);
            }else{
                GestioneServlet.inviaRisposta(request,response,200,taglie.toString(),true,false);
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
        Map<Integer,QueryFields<? extends Comparable<?>>> data = new HashMap<>();
        try{
            data.put(0,new QueryFields<>("tagliaEu",object.getString("tagliaEu"),TipoVariabile.string));
            data.put(1,new QueryFields<>("tagliaUk",object.getString("tagliaUk"),TipoVariabile.string));
            data.put(2,new QueryFields<>("tagliaUs",object.getString("tagliaUs"),TipoVariabile.string));
        }catch(SQLException exception){
            exception.printStackTrace();
            GestioneServlet.inviaRisposta(request,response,500,"\"Internal server error.\"",false,false);
            return;
        }
        if (controller.updateObject(id,data)){
            GestioneServlet.inviaRisposta(request,response,200,"\"Product updated correctly.\"",true,false);
        }else{
            GestioneServlet.inviaRisposta(request,response,500,"\"Internal server error.\"",false,false);
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
        int idTaglia= Integer.parseInt((String) request.getAttribute("idtaglia"));
        if (this.controller.deleteObject(idTaglia)){
            GestioneServlet.inviaRisposta(request,response,200,"\"Product deleted correctly.\"",true,false);
        }else{
            GestioneServlet.inviaRisposta(request,response,500,"\"Internal server error.\"",false,false);
        }
    }

}