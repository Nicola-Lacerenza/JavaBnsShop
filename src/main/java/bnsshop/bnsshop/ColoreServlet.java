package bnsshop.bnsshop;

import controllers.ColoreController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Colore;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ColoreServlet", value = "/ColoreServlet")
public class ColoreServlet extends HttpServlet{
    ColoreController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ColoreController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idColore;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                idColore = Integer.parseInt(idString);
            } else {
                idColore = -1;
            }
        }catch(NumberFormatException exception){
            idColore=-1;
        }
        Optional<Colore> colore=null;
        List<Colore> colori=null;

        if (idColore!=-1){
            colore = this.controller.getObject(idColore);
        }else{
            colori = this.controller.getAllObjects();
        }

        if (colore!=null || colori!=null){
            if (colore!=null){
                GestioneServlet.inviaRisposta(response,200,colore.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,colori.toString(),true);
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
        String nome= object.getString("nome");
        String rgb= object.getString("rgb");
        String hex= object.getString("hex");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(1,new RegisterServlet.RegisterFields("rgb",rgb));
        request0.put(2,new RegisterServlet.RegisterFields("hex",hex));
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
        data.put(1,new RegisterServlet.RegisterFields("rgb","" + object.getString("rgb")));
        data.put(2,new RegisterServlet.RegisterFields("hex","" + object.getString("hex")));
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
        int idColore= Integer.parseInt((String) request.getAttribute("idcolore"));
        if (this.controller.deleteObject(idColore)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}