package bnsshop.bnsshop;

import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.Criptografia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
        String nome= object.getString("nome");
        String cognome= object.getString("cognome");
        String dataNascita= object.getString("data_nascita");
        String luogoNascita= object.getString("luogo_nascita");
        String sesso= object.getString("sesso");
        String email= object.getString("email");
        String telefono= object.getString("telefono");
        String password= object.getString("password");
        String passwordHashed= Criptografia.get_SHA_512_SecurePassword(password,"");
        Map<String,String> request1 = new HashMap<>();
        request1.put("nome",nome);
        request1.put("cognome",cognome);
        request1.put("data_nascita",dataNascita);
        request1.put("luogo_nascita",luogoNascita);
        request1.put("sesso",sesso);
        request1.put("email",email);
        request1.put("telefono",telefono);
        request1.put("password",passwordHashed);
        Optional<String> output = controller.insertObject(request1);
        if (output.isPresent()) {
            String message = output.get();
            JSONObject json1 = new JSONObject();
            json1.put("message", message);
            PrintWriter writer = response.getWriter();
            response.setContentLength(json1.toString().length());
            response.setContentType("application/json");
            //response.setStatus(200);
            writer.println(json1.toString());
            writer.flush();
            writer.close();
        }else{
            String message = "Errore durante la registrazione";
            JSONObject json1 = new JSONObject();
            json1.put("message", message);
            PrintWriter writer = response.getWriter();
            response.setContentLength(json1.toString().length());
            response.setContentType("application/json");
            response.setStatus(500);
            writer.println(json1.toString());
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

}