package bnsshop.bnsshop;

import controllers.CategoriaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Categoria;
import org.json.JSONObject;
import utility.GestioneServlet;
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
            String idString = request.getParameter("id");
            if (idString != null) {
                idCategoria = Integer.parseInt(idString);
            } else {
                idCategoria = -1;
            }
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
                GestioneServlet.inviaRisposta(response,200,categoria.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,categorie.toString(),true);
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
        String nomeCategoria= object.getString("nome_categoria");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("nome_categoria",nomeCategoria));
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
        data.put(0,new RegisterServlet.RegisterFields("nome_categoria","" + object.getString("nome_categoria")));
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
        int idBrand= Integer.parseInt((String) request.getAttribute("idbrand"));
        if (this.controller.deleteObject(idBrand)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}