package bnsshop.bnsshop;

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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet{

    @Override
    public void init() throws ServletException{
        super.init();
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
        String indirizzoEmail= object.getString("email");
        String telefono= object.getString("telefono");
        String password= object.getString("password");
        String passwordHashed= Criptografia.get_SHA_512_SecurePassword(password,"");

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