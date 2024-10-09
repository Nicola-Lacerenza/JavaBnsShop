package bnsshop.bnsshop;

import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Utenti;
import org.json.JSONObject;
import utility.Crittografia;
import utility.GestioneServlet;
import utility.GestioneToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class AuthServlet extends HttpServlet{

    private UtentiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new UtentiController();
    }

    // Gestione richiesta preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
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
        String username= object.getString("username");
        String password= object.getString("password");

        String query = "SELECT * FROM utenti WHERE email='"+username+"'";
        List<Utenti> utenti = controller.executeQuery(query);
        if (utenti.isEmpty()){
            GestioneServlet.inviaRisposta(response,404,"\"Utente non trovato!\"",false);
        }else{
            Utenti utente = utenti.getFirst();
            Optional<String> passwordHashed= Crittografia.get_SHA_512_SecurePassword(password,"1234");
            if (passwordHashed.isPresent()){
                if (!passwordHashed.get().equals(utente.getPassword())){
                    GestioneServlet.inviaRisposta(response,400,"\"La password è sbagliata!\"",false);
                }else{
                    String token= GestioneToken.createToken(username);
                    GestioneServlet.inviaRisposta(response,200,"\""+token+"\"",true);
                }
            }else{
                GestioneServlet.inviaRisposta(response,400,"\"La password è sbagliata!\"",false);
            }
        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }
}