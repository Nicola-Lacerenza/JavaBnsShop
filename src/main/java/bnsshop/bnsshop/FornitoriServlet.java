package bnsshop.bnsshop;

import controllers.FornitoriController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Fornitori;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "FornitoriServlet", value = "/FornitoriServlet")
public class FornitoriServlet extends HttpServlet{
    FornitoriController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new FornitoriController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idFornitore;
        try{
            idFornitore = Integer.parseInt((String) request.getAttribute("idfornitore"));
        }catch(NumberFormatException exception){
            idFornitore=-1;
        }
        Optional<Fornitori> fornitore=null;
        List<Fornitori> fornitori=null;

        if (idFornitore!=-1){
            fornitore = this.controller.getObject(idFornitore);
        }else{
            fornitori = this.controller.getAllObjects();
        }

        if (fornitore!=null || fornitori!=null){
            if (fornitore!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(fornitore.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(fornitore.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(fornitori.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(fornitori.toString());
                writer.flush();
                writer.close();
            }
        }else{
            String message = "Internal server error";
            JSONObject error = new JSONObject();
            error.put("message", message);
            PrintWriter writer= response.getWriter();
            response.setContentLength(error.toString().length());
            response.setContentType("application/json");
            response.setStatus(500);
            writer.println(error.toString());
            writer.flush();
            writer.close();
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
        Map<String,String> data= new HashMap<>();
        data.put("idfornitore","" + object.getInt("idFornitore"));
        data.put("nome","" + object.getString("nome"));
        data.put("cognome","" + object.getString("cognome"));
        if (controller.insertObject(data)){
            String fornitore = "\"Fornitore creato correttamente.\"";
            PrintWriter writer= response.getWriter();
            response.setContentLength(fornitore.toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(fornitore.toString());
            writer.flush();
            writer.close();
        }else{
            String message = "Internal server error";
            JSONObject error = new JSONObject();
            error.put("message", message);
            PrintWriter writer= response.getWriter();
            response.setContentLength(error.toString().length());
            response.setContentType("application/json");
            response.setStatus(500);
            writer.println(error.toString());
            writer.flush();
            writer.close();
        }

    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
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
        Map<String,String> data= new HashMap<>();
        data.put("idfornitore","" + object.getInt("idfornitore"));
        data.put("nome","" + object.getString("nome"));
        data.put("cognome","" + object.getString("cognome"));
        if (controller.updateObject(data)){
            String message="Product Updated Correctly.";
            PrintWriter writer= response.getWriter();
            response.setContentLength(message.length());
            response.setContentType("application/json");
            response.setStatus(200);
            writer.println(message);
            writer.flush();
            writer.close();
        }else{
            String message = "Internal server error";
            JSONObject error = new JSONObject();
            error.put("message", message);
            PrintWriter writer= response.getWriter();
            response.setContentLength(error.toString().length());
            response.setContentType("application/json");
            response.setStatus(500);
            writer.println(error.toString());
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        int idFornitore = Integer.parseInt((String) request.getAttribute("idfornitore"));
        if (this.controller.deleteObject(idFornitore)){
            String message = "Product deleted Correctly.";
            PrintWriter writer= response.getWriter();
            response.setContentLength(message.length());
            response.setContentType("application/json");
            response.setStatus(200);
            writer.println(message);
            writer.flush();
            writer.close();
        }else{
            String message = "Internal server error";
            JSONObject error = new JSONObject();
            error.put("message", message);
            PrintWriter writer= response.getWriter();
            response.setContentLength(error.toString().length());
            response.setContentType("application/json");
            response.setStatus(500);
            writer.println(error.toString());
            writer.flush();
            writer.close();
        }
    }

}