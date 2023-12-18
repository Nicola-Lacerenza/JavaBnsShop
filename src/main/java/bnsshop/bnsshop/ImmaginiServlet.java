package bnsshop.bnsshop;

import controllers.ImmaginiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Immagini;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ImmaginiServlet", value = "/ImmaginiServlet")
public class ImmaginiServlet extends HttpServlet{
    ImmaginiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ImmaginiController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idImmagini;
        try{
            idImmagini = Integer.parseInt((String) request.getAttribute("idimmagini"));
        }catch(NumberFormatException exception){
            idImmagini=-1;
        }
        Optional<Immagini> immagine=null;
        List<Immagini> immagini=null;

        if (idImmagini!=-1){
            immagine = this.controller.getObject(idImmagini);
        }else{
            immagini = this.controller.getAllObjects();
        }

        if (immagine!=null || immagini!=null){
            if (immagine!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(immagine.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(immagine.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(immagini.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(immagini.toString());
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
        data.put("idimmagini","" + object.getInt("idImmagini"));
        data.put("idprodotti","" + object.getInt("idprodotti"));
        data.put("url","" + object.getString("url"));
        Optional<Immagini> immagine = controller.insertObject(data);
        if (immagine.isPresent()){
            PrintWriter writer= response.getWriter();
            response.setContentLength(immagine.get().toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(immagine.get().toString());
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
        data.put("idimmagini","" + object.getInt("idImmagini"));
        data.put("idprodotti","" + object.getInt("idprodotti"));
        data.put("url","" + object.getString("url"));
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
        int idImmagini= Integer.parseInt((String) request.getAttribute("idimmagini"));
        if (this.controller.deleteObject(idImmagini)){
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