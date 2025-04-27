package bnsshop.bnsshop;

import controllers.CodiceScontoController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CodiceSconto;
import org.json.JSONObject;
import utility.GestioneServlet;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "CodiceScontoServlet", value = "/CodiceScontoServlet")
public class CodiceScontoServlet extends HttpServlet{
    CodiceScontoController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new CodiceScontoController();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        if(GestioneServlet.aggiungiCorsSicurezzaHeadersDynamicPage(request,response)){
            System.out.println("CORS and security headers added correctly in the response.");
        }else{
            System.err.println("Error writing the CORS and security headers in the response.");
        }
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
        Optional<CodiceSconto> codiceSconto=Optional.empty();
        List<CodiceSconto> codiciSconto=null;

        if (id!=-1){
            codiceSconto = this.controller.getObject(id);
        }else{
            codiciSconto = this.controller.getAllObjects();
        }

        if (codiceSconto.isPresent() || codiciSconto!=null){
            if (codiceSconto.isPresent()){
                GestioneServlet.inviaRisposta(request,response,200,codiceSconto.get().toString(),true,false);
            }else{
                GestioneServlet.inviaRisposta(request,response,200,codiciSconto.toString(),true,false);
            }
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
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
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
            return;
        }
        BufferedReader reader=request.getReader();
        String row=reader.readLine();
        List<String> rows = new LinkedList<>();
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
        int idCategoria = object.getInt("categoria");
        String codice = object.getString("codice");
        int valore = object.getInt("valore");
        String descrizione = object.getString("descrizione");
        String tipo = object.getString("tipo");
        String dataInizio= object.getString("data_inizio");
        String dataFine= object.getString("data_fine");
        int usoMassimo= object.getInt("uso_massimo");
        int usoPerUtente= object.getInt("uso_per_utente");
        int minimoAcquisto = object.getInt("minimo_acquisto");
        int attivo= object.getInt("attivo");

        Map<Integer,QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
        try{
            request0.put(0,new QueryFields<>("codice",codice,TipoVariabile.string));
            request0.put(1, new QueryFields<>("valore",valore,TipoVariabile.longNumber));
            request0.put(2,new QueryFields<>("descrizione",descrizione,TipoVariabile.string));
            request0.put(3,new QueryFields<>("tipo",tipo,TipoVariabile.string));
            request0.put(4,new QueryFields<>("data_inizio",dataInizio,TipoVariabile.string));
            request0.put(5,new QueryFields<>("data_fine",dataFine,TipoVariabile.string));
            request0.put(6,new QueryFields<>("uso_massimo",usoMassimo,TipoVariabile.longNumber));
            request0.put(7,new QueryFields<>("uso_per_utente",usoPerUtente,TipoVariabile.longNumber));
            request0.put(8,new QueryFields<>("minimo_acquisto",minimoAcquisto,TipoVariabile.longNumber));
            request0.put(9,new QueryFields<>("attivo",attivo,TipoVariabile.longNumber));
            request0.put(10,new QueryFields<>("categoria",idCategoria,TipoVariabile.longNumber));
        }catch(SQLException exception){
            exception.printStackTrace();
            GestioneServlet.inviaRisposta(request,response,500,"\"Internal server error.\"",false,false);
            return;
        }
        int idCodiceSconto = controller.insertObject(request0);
        if (idCodiceSconto > 0) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(request,response,201,registrazione,true,false);
        }else{
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
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
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
            return;
        }
        int id= Integer.parseInt(request.getParameter("id"));
        BufferedReader reader=request.getReader();
        String row=reader.readLine();
        List<String> rows = new LinkedList<>();
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
        Map<Integer,QueryFields<? extends Comparable<?>>> data = new HashMap<>();
        try{
            data.put(0,new QueryFields<>("codice",object.getString("codice"),TipoVariabile.string));
            data.put(1,new QueryFields<>("valore",object.getInt("valore"),TipoVariabile.longNumber));
            data.put(2,new QueryFields<>("descrizione",object.getString("descrizione"),TipoVariabile.string));
            data.put(3,new QueryFields<>("tipo",object.getString("tipo"),TipoVariabile.string));
            data.put(4,new QueryFields<>("data_inizio",object.getString("data_inizio"),TipoVariabile.string));
            data.put(5,new QueryFields<>("data_fine",object.getString("data_fine"),TipoVariabile.string));
            data.put(6,new QueryFields<>("uso_massimo",object.getInt("uso_massimo"),TipoVariabile.longNumber));
            data.put(7,new QueryFields<>("uso_per_utente",object.getInt("uso_per_utente"),TipoVariabile.longNumber));
            data.put(8,new QueryFields<>("minimo_acquisto",object.getInt("minimo_acquisto"),TipoVariabile.longNumber));
            data.put(9,new QueryFields<>("attivo",object.getInt("attivo"),TipoVariabile.longNumber));
            data.put(10,new QueryFields<>("categoria",object.getString("categoria"),TipoVariabile.string));
        }catch (SQLException exception){
            exception.printStackTrace();
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            return;
        }
        if (controller.updateObject(id,data)){
            String message="\"Product Updated Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
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
            GestioneServlet.inviaRisposta(request,response,403,"\"Ruolo non corretto!\"",false,false);
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(request,response,200,message,true,false);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }
}