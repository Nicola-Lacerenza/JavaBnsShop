package bnsshop.bnsshop;

import controllers.ColoreController;
import controllers.ModelloController;
import controllers.TagliaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Colore;
import models.Modello;
import models.Taglia;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "ModelloServlet", value = "/ModelloServlet")
public class ModelloServlet extends HttpServlet{
    ModelloController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ModelloController();
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
            }
        }catch(NumberFormatException exception){
            id=-1;
        }
        Optional<Modello> modello=null;
        List<Modello> modelli=null;

        if (id!=-1){
            modello = this.controller.getObject(id);
        }else{
            modelli = this.controller.getAllObjects();
        }

        if (modello!=null || modelli!=null){
            if (modello!=null){
                GestioneServlet.inviaRisposta(response,200,modello.get().toString(),true);
            }else{
                GestioneServlet.inviaRisposta(response,200,modelli.toString(),true);
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
        String idCategoria= object.getString("id_categoria");
        String idBrand= object.getString("id_brand");
        String colore= object.getString("colore");
        String nome= object.getString("nome");
        String descrizione= object.getString("descrizione");

        //Estrazione ID Colore
        // Estrazione degli ID Colore
        ColoreController coloreController = new ColoreController();
        List<Colore> listColore = coloreController.getAllObjects();

        // Se la stringa dei colori contiene più colori separati da una virgola
        String[] coloriArray = colore.split(",");

        // Lista per contenere gli ID dei colori
        List<Integer> idColori = new ArrayList<>();

        // Itera su tutti i colori passati nella stringa
        for (String coloreString : coloriArray) {
            String coloreTrimmed = coloreString.trim();  // Rimuovi eventuali spazi bianchi
            List<Integer> coloriTrovati = listColore.stream()
                    .filter(colore1 -> colore1.getNome().equalsIgnoreCase(coloreTrimmed)) // Usa equalsIgnoreCase per un confronto più robusto
                    .map(Colore::getId)
                    .collect(Collectors.toList());

            if (!coloriTrovati.isEmpty()) {
                idColori.addAll(coloriTrovati); // Aggiungi tutti gli ID trovati
            } else {
                // Gestione errore se un colore non viene trovato
                String message = "\"Errore: Colore " + coloreTrimmed + " non trovato.\"";
                GestioneServlet.inviaRisposta(response, 500, message, false);
                return;
            }
        }

        if (idColori.isEmpty()) {
            String message = "\"Errore durante la registrazione: nessun colore valido trovato.\"";
            GestioneServlet.inviaRisposta(response, 500, message, false);
            return;
        }
        Map<Integer, RegisterServlet.RegisterFields> request0= new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("id_categoria",idCategoria));
        request0.put(1,new RegisterServlet.RegisterFields("id_brand",idBrand));
        request0.put(2,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(3,new RegisterServlet.RegisterFields("descrizione",descrizione));
        // Inserisci tutti i colori nella mappa
        for (int i = 0; i < idColori.size(); i++) {
            request0.put(4 + i, new RegisterServlet.RegisterFields("colore_" + (i + 1), String.valueOf(idColori.get(i))));
        }
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
        data.put(0,new RegisterServlet.RegisterFields("id_categoria","" + object.getString("id_categoria")));
        data.put(1,new RegisterServlet.RegisterFields("id_brand","" + object.getString("id_brand")));
        data.put(2,new RegisterServlet.RegisterFields("nome","" + object.getString("nome")));
        data.put(3,new RegisterServlet.RegisterFields("descrizione","" + object.getString("descrizione")));
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
        int id= Integer.parseInt((String) request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

}