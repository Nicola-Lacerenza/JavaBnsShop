package bnsshop.bnsshop;

import controllers.TagliaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Taglia;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "TagliaServlet", value = "/TagliaServlet")
public class TagliaServlet extends HttpServlet{
    TagliaController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new TagliaController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idTaglia;
        try{
            idTaglia = Integer.parseInt((String) request.getAttribute("idtaglia"));
        }catch(NumberFormatException exception){
            idTaglia=-1;
        }
        Optional<Taglia> taglia=null;
        List<Taglia> taglie=null;

        if (idTaglia!=-1){
            taglia = this.controller.getObject(idTaglia);
        }else{
            taglie = this.controller.getAllObjects();
        }

        if (taglia!=null || taglie!=null){
            if (taglia!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(taglia.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(taglia.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(taglie.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(taglie.toString());
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
        data.put("idtaglia","" + object.getInt("idTaglia"));
        data.put("taglia","" + object.getString("taglia"));
        if (controller.insertObject(data)){
            String taglia = "\"Taglia creata correttamente.\"";
            PrintWriter writer= response.getWriter();
            response.setContentLength(taglia.toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(taglia.toString());
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
        data.put("idtaglia","" + object.getInt("idtaglia"));
        data.put("taglia","" + object.getString("taglia"));
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
        int idTaglia= Integer.parseInt((String) request.getAttribute("idtaglia"));
        if (this.controller.deleteObject(idTaglia)){
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