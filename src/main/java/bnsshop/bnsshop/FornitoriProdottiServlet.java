package bnsshop.bnsshop;

import controllers.FornitoriProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.FornitoriProdotti;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "FornitoriProdottiServlet", value = "/FornitoriProdottiServlet")
public class FornitoriProdottiServlet extends HttpServlet{
    FornitoriProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new FornitoriProdottiController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idFornitore;
        try{
            idFornitore = Integer.parseInt((String) request.getAttribute("idfornitore"));
        }catch(NumberFormatException exception){
            idFornitore=-1;
        }
        Optional<FornitoriProdotti> fornitoreprodotto=null;
        List<FornitoriProdotti> fornitoriprodotti=null;

        if (idFornitore!=-1){
            fornitoreprodotto = this.controller.getObject(idFornitore);
        }else{
            fornitoriprodotti = this.controller.getAllObjects();
        }

        if (fornitoreprodotto!=null || fornitoriprodotti!=null){
            if (fornitoreprodotto!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(fornitoreprodotto.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(fornitoreprodotto.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(fornitoriprodotti.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(fornitoriprodotti.toString());
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
        data.put("idprodotti","" + object.getInt("idProdotti"));
        data.put("data","" + object.get("data"));
        data.put("impporto","" + object.getInt("impporto"));
        data.put("descrizione","" + object.getString("descrizione"));
        Optional<FornitoriProdotti> fornitoreprodotto = controller.insertObject(data);
        if (fornitoreprodotto.isPresent()){
            PrintWriter writer= response.getWriter();
            response.setContentLength(fornitoreprodotto.get().toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(fornitoreprodotto.get().toString());
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
        data.put("idprodotti","" + object.getInt("idprodotti"));
        data.put("data","" + object.get("data"));
        data.put("importo","" + object.getInt("importo"));
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