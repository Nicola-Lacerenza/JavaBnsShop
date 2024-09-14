package bnsshop.bnsshop;

import controllers.ColoreModelloController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ColoreModello;
import org.json.JSONObject;
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
        int idColore;
        int idModello;
        try{
            idColore = Integer.parseInt((String) request.getAttribute("idcolore"));
        }catch(NumberFormatException exception){
            idColore=-1;
        }
        Optional<ColoreModello> coloremodello=null;
        List<ColoreModello> colorimodelli=null;

        if (idColore!=-1){
            coloremodello = this.controller.getObject(idColore);
        }else{
            colorimodelli = this.controller.getAllObjects();
        }

        if (coloremodello!=null || colorimodelli!=null){
            if (coloremodello!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(coloremodello.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(coloremodello.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(colorimodelli.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(colorimodelli.toString());
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
        data.put("idmodello","" + object.getInt("idModello"));
        if (controller.insertObject(data)){
            String coloremodello = "Relazione tra colore e modello creata correttamente.";
            PrintWriter writer= response.getWriter();
            response.setContentLength(coloremodello.toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(coloremodello.toString());
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
        data.put("idmodello","" + object.getInt("idmodello"));
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