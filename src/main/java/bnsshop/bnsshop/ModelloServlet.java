package bnsshop.bnsshop;

import controllers.ModelloController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Modello;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ModelloServlet", value = "/ModelloServlet")
public class ModelloServlet extends HttpServlet{
    ModelloController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ModelloController();
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
        Optional<Modello> modello=null;
        List<Modello> modelli=null;

        if (id!=-1){
            modello = this.controller.getObject(id);
        }else{
            modelli = this.controller.getAllObjects();
        }

        if (modello!=null || modelli!=null){
            if (modello!=null){
                GestioneServlet.inviaRisposta(response,200,modello.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,modelli.toString(),true);
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
        String idCategoria= object.getString("id_categoria");
        String idBrand= object.getString("id_brand");
        String nome= object.getString("nome");
        String descrizione= object.getString("descrizione");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_categoria",idCategoria));
        request0.put(1,new RegisterServlet.RegisterFields("id_brand",idBrand));
        request0.put(2,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(3,new RegisterServlet.RegisterFields("descrizione",descrizione));
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
        data.put(0,new RegisterServlet.RegisterFields("id_categoria","" + object.getString("id_categoria")));
        data.put(1,new RegisterServlet.RegisterFields("id_brand","" + object.getString("id_brand")));
        data.put(2,new RegisterServlet.RegisterFields("nome","" + object.getString("nome")));
        data.put(3,new RegisterServlet.RegisterFields("descrizione","" + object.getString("descrizione")));
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
        int id= Integer.parseInt((String) request.getAttribute("idmodello"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}