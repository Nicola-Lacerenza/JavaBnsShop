package bnsshop.bnsshop;

import controllers.FornitoriProdottiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.FornitoriProdotti;
import org.json.JSONObject;
import utility.GestioneServlet;

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
        int id;
        try{
            String idString = request.getParameter("id");
            if (idString != null) {
                id = Integer.parseInt(idString);
            } else {
                id = -1;
            }
        }catch(NumberFormatException exception){
            id=-1;
        }
        Optional<FornitoriProdotti> fornitoreprodotto=null;
        List<FornitoriProdotti> fornitoriprodotti=null;

        if (id!=-1){
            fornitoreprodotto = this.controller.getObject(id);
        }else{
            fornitoriprodotti = this.controller.getAllObjects();
        }

        if (fornitoreprodotto!=null || fornitoriprodotti!=null){
            if (fornitoreprodotto!=null){
                GestioneServlet.inviaRisposta(response,200,fornitoreprodotto.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,fornitoriprodotti.toString(),true);
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
        String idProdotti= object.getString("id_prodotti");
        String idFornitore= object.getString("id_fornitore");
        String data= object.getString("data");
        String importo= object.getString("importo");
        String descrizione= object.getString("descrizione");
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_prodotti",idProdotti));
        request0.put(1,new RegisterServlet.RegisterFields("id_fornitore",idFornitore));
        request0.put(2,new RegisterServlet.RegisterFields("data",data));
        request0.put(3,new RegisterServlet.RegisterFields("importo",importo));
        request0.put(4,new RegisterServlet.RegisterFields("descrizione",descrizione));
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
        data.put(0,new RegisterServlet.RegisterFields("id_fornitore","" + object.getString("id_fornitore")));
        data.put(1,new RegisterServlet.RegisterFields("id_prodotti","" + object.getString("id_prodotti")));
        data.put(2,new RegisterServlet.RegisterFields("data","" + object.getString("data")));
        data.put(3,new RegisterServlet.RegisterFields("importo","" + object.getString("importo")));
        data.put(4,new RegisterServlet.RegisterFields("descrizione","" + object.getString("descrizione")));
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
        int id = Integer.parseInt((String) request.getAttribute("idfornitore"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}