package bnsshop.bnsshop;

import controllers.ProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebServlet;
import models.Prodotti;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

@WebServlet(name = "ProdottiServlet", value = "/ProdottiServlet")
public class ProdottiServlet extends HttpServlet{
    ProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ProdottiController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
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
        data.put("idprodotto","" + object.getInt("idprodotto"));
        data.put("idmodello","" + object.getInt("idmodello"));
        data.put("idtaglia","" + object.getInt("idtaglia"));
        data.put("prezzo","" + object.getDouble("prezzo"));
        data.put("quantita","" + object.getInt("quantita"));
        data.put("statopubblicazione","" + object.getInt("statopubblicazione"));
        Optional<Prodotti> prodotto = controller.insertObject(data);
        if (prodotto.isPresent()){
            PrintWriter writer= response.getWriter();
            response.setContentLength(prodotto.get().toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(prodotto.get().toString());
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
        data.put("idprodotto","" + object.getInt("idprodotto"));
        data.put("idmodello","" + object.getInt("idmodello"));
        data.put("idtaglia","" + object.getInt("idtaglia"));
        data.put("prezzo","" + object.getDouble("prezzo"));
        data.put("quantita","" + object.getInt("quantita"));
        data.put("statopubblicazione","" + object.getInt("statopubblicazione"));
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
        response.getWriter().println("Hello world!");
        response.getWriter().flush();
        response.getWriter().close();
    }
}