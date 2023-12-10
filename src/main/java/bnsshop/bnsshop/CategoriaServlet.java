package bnsshop.bnsshop;

import controllers.CategoriaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Categoria;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "CategoriaServlet", value = "/CategoriaServlet")
public class CategoriaServlet extends HttpServlet{
    CategoriaController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new CategoriaController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        int idCategoria;
        try{
            idCategoria = Integer.parseInt((String) request.getAttribute("idbrand"));
        }catch(NumberFormatException exception){
            idCategoria=-1;
        }
        Optional<Categoria> categoria=null;
        List<Categoria> categorie=null;

        if (idCategoria!=-1){
            categoria = this.controller.getObject(idCategoria);
        }else{
            categorie = this.controller.getAllObjects();
        }

        if (categoria!=null || categorie!=null){
            if (categoria!=null){
                PrintWriter writer= response.getWriter();
                response.setContentLength(categoria.get().toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(categoria.get().toString());
                writer.flush();
                writer.close();
            }else{
                PrintWriter writer= response.getWriter();
                response.setContentLength(categorie.toString().length());
                response.setContentType("application/json");
                response.setStatus(200);
                writer.println(categorie.toString());
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
        data.put("idcategoria","" + object.getInt("idCategoria"));
        data.put("nomecategoria","" + object.getString("nomecategoria"));
        Optional<Categoria> categoria = controller.insertObject(data);
        if (categoria.isPresent()){
            PrintWriter writer= response.getWriter();
            response.setContentLength(categoria.get().toString().length());
            response.setContentType("application/json");
            response.setStatus(201);
            writer.println(categoria.get().toString());
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
        data.put("idbrand","" + object.getInt("idbrand"));
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
        int idBrand= Integer.parseInt((String) request.getAttribute("idbrand"));
        if (this.controller.deleteObject(idBrand)){
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