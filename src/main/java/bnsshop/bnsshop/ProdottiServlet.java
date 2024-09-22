package bnsshop.bnsshop;

import controllers.ProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebServlet;
import models.Prodotti;
import org.json.JSONObject;
import utility.GestioneServlet;

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
        int id;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                id = Integer.parseInt(idString);
            } else {
                id = -1;
            }        }catch(NumberFormatException exception){
            id=-1;
        }
        Optional<Prodotti> prodotto=null;
        List<Prodotti> prodotti=null;

        if (id!=-1){
            prodotto = this.controller.getObject(id);
        }else{
            prodotti = this.controller.getAllObjects();
        }

        if (prodotto!=null || prodotti!=null){
            if (prodotto!=null){
                GestioneServlet.inviaRisposta(response,200,prodotto.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,prodotti.toString(),true);
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
        String idModello= object.getString("id_modello");
        String idTaglia= object.getString("id_taglia");
        String prezzo= object.getString("prezzo");
        String quantita= object.getString("quantita");
        String statoPubblicazione= object.getString("stato_pubblicazione");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_modello",idModello));
        request0.put(1,new RegisterServlet.RegisterFields("id_taglia",idTaglia));
        request0.put(2,new RegisterServlet.RegisterFields("prezzo",prezzo));
        request0.put(3,new RegisterServlet.RegisterFields("quantita",quantita));
        request0.put(4,new RegisterServlet.RegisterFields("stato_pubblicazione",statoPubblicazione));
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
        data.put(0,new RegisterServlet.RegisterFields("id_modello","" + object.getString("id_modello")));
        data.put(1,new RegisterServlet.RegisterFields("id_taglia","" + object.getString("id_taglia")));
        data.put(2,new RegisterServlet.RegisterFields("prezzo","" + object.getString("prezzo")));
        data.put(3,new RegisterServlet.RegisterFields("quantita","" + object.getString("quantita")));
        data.put(4,new RegisterServlet.RegisterFields("stato_pubblicazione","" + object.getString("stato_pubblicazione")));
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
        int id= Integer.parseInt((String) request.getAttribute("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}