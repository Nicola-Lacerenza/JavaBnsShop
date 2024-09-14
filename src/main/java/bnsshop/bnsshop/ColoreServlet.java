package bnsshop.bnsshop;

import controllers.ColoreController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Colore;
import org.json.JSONObject;
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
            idColore = Integer.parseInt((String) request.getAttribute("idcolore"));
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
                PrintWriter writer= response.getWriter();
                response.setContentLength(colore.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(colore.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(colori.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(colori.toString());
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
        data.put("idcolore","" + object.getInt("idColore"));
        data.put("nome","" + object.getString("nome"));
        data.put("rgb","" + object.getString("rgb"));
        data.put("hex","" + object.getString("hex"));
        if (controller.insertObject(data)){
            String colore = "\"Colore creato correttamente.\"";
            PrintWriter writer= response.getWriter();
            response.setContentLength(colore.toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(colore.toString());
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
        data.put("idcolore","" + object.getInt("idcolore"));
        data.put("nome","" + object.getString("nome"));
        data.put("rgb","" + object.getString("rgb"));
        data.put("hex","" + object.getString("hex"));
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
        int idColore= Integer.parseInt((String) request.getAttribute("idcolore"));
        if (this.controller.deleteObject(idColore)){
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