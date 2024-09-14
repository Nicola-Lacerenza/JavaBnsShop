package bnsshop.bnsshop;

import controllers.ModelloController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Modello;
import org.json.JSONObject;
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
        int idModello;
        try{
            idModello = Integer.parseInt((String) request.getAttribute("idmodello"));
        }catch(NumberFormatException exception){
            idModello=-1;
        }
        Optional<Modello> modello=null;
        List<Modello> modelli=null;

        if (idModello!=-1){
            modello = this.controller.getObject(idModello);
        }else{
            modelli = this.controller.getAllObjects();
        }

        if (modello!=null || modelli!=null){
            if (modello!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(modello.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(modello.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(modelli.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(modelli.toString());
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
        data.put("idmodello","" + object.getInt("idModello"));
        data.put("idbrand","" + object.getInt("idBrand"));
        data.put("idcategoria","" + object.getInt("idCategoria"));
        data.put("nome","" + object.getString("nome"));
        data.put("descrizione","" + object.getString("descrizione"));
        if (controller.insertObject(data)){
            String modello = "\"modello creato correttamente.\"";
            PrintWriter writer= response.getWriter();
            response.setContentLength(modello.toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(modello.toString());
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
        data.put("idmodello","" + object.getInt("idModello"));
        data.put("idbrand","" + object.getInt("idBrand"));
        data.put("idcategoria","" + object.getInt("idCategoria"));
        data.put("nome","" + object.getString("nome"));
        data.put("descrizione","" + object.getString("descrizione"));
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
        int idModello= Integer.parseInt((String) request.getAttribute("idmodello"));
        if (this.controller.deleteObject(idModello)){
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