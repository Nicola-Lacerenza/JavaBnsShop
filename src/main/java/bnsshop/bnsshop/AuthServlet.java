package bnsshop.bnsshop;

import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class AuthServlet extends HttpServlet{

    UtentiController controller;

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
        // Legge il corpo della richiesta
        BufferedReader reader=request.getReader();
        String row=reader.readLine();
        List<String> rows = new ArrayList<>();
        while (row!=null){
            rows.add(row);
            row=reader.readLine();
        }

        // Costruisce una stringa JSON concatenando tutte le righe lette
        StringBuilder builder= new StringBuilder();
        for (String line:rows){
            builder.append(line);
        }

        String json=builder.toString();

        // Converte la stringa JSON in un oggetto JSONObject
        JSONObject object = new JSONObject(json);
        String userId= object.getString("userId");
        String password= object.getString("password");

        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200/" );
        //response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD" );
        //response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER" );
        //response.addHeader("Access-Control-Max-Age", "1728000" );
        String message = "prova";
        JSONObject json1 = new JSONObject();
        json1.put("message",message);
        PrintWriter writer= response.getWriter();
        response.setContentLength(json1.toString().length());
        response.setContentType("application/json");
        //response.setStatus(200);
        writer.println(json1.toString());
        writer.flush();
        writer.close();
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

}