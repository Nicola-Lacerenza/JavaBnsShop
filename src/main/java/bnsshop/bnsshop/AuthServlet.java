package bnsshop.bnsshop;

import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.GestioneServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class AuthServlet extends HttpServlet{

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
        String username= object.getString("username");
        String password= object.getString("password");

        //implementare qui il login vero e proprio

        GestioneServlet.inviaRisposta(response,501,"\"Funzione non disponibile\"",false);
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }
}