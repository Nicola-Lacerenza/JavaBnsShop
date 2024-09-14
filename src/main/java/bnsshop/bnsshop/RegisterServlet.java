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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet{

    private UtentiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new UtentiController();
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
        String nome= object.getString("nome");
        String cognome= object.getString("cognome");
        String dataNascita= object.getString("data_nascita");
        String luogoNascita= object.getString("luogo_nascita");
        String sesso= object.getString("sesso");
        String email= object.getString("email");
        String telefono= object.getString("telefono");
        String password= object.getString("password");
        Optional<String> passwordHashed= Crittografia.get_SHA_512_SecurePassword(password,"");
        if(passwordHashed.isPresent()){
            Map<String,String> request1 = new HashMap<>();
            request1.put("nome",nome);
            request1.put("cognome",cognome);
            request1.put("data_nascita",dataNascita);
            request1.put("luogo_nascita",luogoNascita);
            request1.put("sesso",sesso);
            request1.put("email",email);
            request1.put("telefono",telefono);
            request1.put("password",passwordHashed.get());
            Optional<Utenti> output = controller.insertObject(request1);

            if (output.isPresent()) {
                GestioneServlet.inviaRisposta(response,201,output.get().toString(),true);
            }else{
                String message = "\"Errore durante la registrazione.\"";
                GestioneServlet.inviaRisposta(response,500,message,false);
            }
        }else{
            String message = "{\"message\":\"Errore durante la registrazione.\",\"check\":false}";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST,GET,PUT,DELETE,HEAD,OPTIONS,TRACE");
        response.addHeader("Access-Control-Allow-Headers","X-CUSTOM,Content-Length,Content-Type");
        response.addHeader("Access-Control-Max-Age","86400");
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }
}