package bnsshop.bnsshop;

import controllers.ImmaginiController;
import controllers.ProdottiController;
import controllers.TagliaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import models.Immagini;
import models.Prodotti;
import models.Taglia;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@WebServlet(name = "ProdottiServlet", value = "/ProdottiServlet")
public class ProdottiServlet extends HttpServlet{
    ProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ProdottiController();
    }

    // Gestione richiesta preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length,Authorization");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
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
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
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
        String url = object.getString("images");
        String idModello= object.getString("id_modello");
        Integer taglia= object.getInt("taglia");
        Integer prezzo= object.getInt("prezzo");
        Integer quantita= object.getInt("quantita");
        Boolean statoPubblicazione= object.getBoolean("stato_pubblicazione");
        int statoPubblicazioneInt = statoPubblicazione ? 1 : 0;

        //Estrazione ID Taglia
        TagliaController tagliaController = new TagliaController();
        List<Taglia> listTaglia = tagliaController.getAllObjects();
        List<Integer> taglie = listTaglia.stream()
                .filter(taglia1 -> taglia1.getTaglia().equals(String.valueOf(taglia)))
                .map(t -> t.getId())
                .toList();
        if (taglie.isEmpty()){
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
            return;
        }
        int idTaglia = taglie.getFirst();

        // Retrieve the file part from the request
        //Part filePart = request.getPart("images");
        //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // Save the file to the server
        //InputStream inputStream = filePart.getInputStream();
        //Files.copy(inputStream, Paths.get(fileName));

        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("url",(url)));
        request0.put(1,new RegisterServlet.RegisterFields("id_modello",idModello));
        request0.put(2,new RegisterServlet.RegisterFields("id_taglia",""+idTaglia));
        request0.put(4,new RegisterServlet.RegisterFields("prezzo",""+prezzo));
        request0.put(5,new RegisterServlet.RegisterFields("quantita",""+quantita));
        request0.put(6,new RegisterServlet.RegisterFields("stato_pubblicazione",""+statoPubblicazioneInt));
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
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
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
        data.put(2,new RegisterServlet.RegisterFields("id_immagini","" + object.getString("id_immagini")));
        data.put(3,new RegisterServlet.RegisterFields("prezzo","" + object.getString("prezzo")));
        data.put(4,new RegisterServlet.RegisterFields("quantita","" + object.getString("quantita")));
        data.put(5,new RegisterServlet.RegisterFields("stato_pubblicazione","" + object.getString("stato_pubblicazione")));
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
        String email = GestioneServlet.validaToken(request,response);
        if (email.isEmpty()){
            return;
        }
        String ruolo = GestioneServlet.controllaRuolo(email);
        if(!ruolo.equals("admin")){
            GestioneServlet.inviaRisposta(response,403,"\"Ruolo non corretto!\"",false);
            return;
        }
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