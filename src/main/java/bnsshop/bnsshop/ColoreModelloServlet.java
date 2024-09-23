package bnsshop.bnsshop;

import controllers.ColoreModelloController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ColoreModello;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ColoreModelloServlet", value = "/ColoreModelloServlet")
public class ColoreModelloServlet extends HttpServlet{
    ColoreModelloController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ColoreModelloController();
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
        Optional<ColoreModello> coloremodello=null;
        List<ColoreModello> colorimodelli=null;

        if (idColoreModello!=-1){
            coloremodello = this.controller.getObject(idColoreModello);
        }else{
            colorimodelli = this.controller.getAllObjects();
        }

        if (coloremodello!=null || colorimodelli!=null){
            if (coloremodello!=null){
                GestioneServlet.inviaRisposta(response,200,coloremodello.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,colorimodelli.toString(),true);
            }
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
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
        String idColore= object.getString("id_colore");
        String idModello= object.getString("id_modello");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_colore",idColore));
        request0.put(1,new RegisterServlet.RegisterFields("id_modello",idModello));
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
        int idCategoria= Integer.parseInt((String) request.getParameter("id"));
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
        data.put(0,new RegisterServlet.RegisterFields("id_colore","" + object.getString("id_colore")));
        data.put(1,new RegisterServlet.RegisterFields("id_modello","" + object.getString("id_modello")));
        if (controller.updateObject(idCategoria,data)){
            String message="\"Product Updated Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        int id= Integer.parseInt((String) request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}