package utility;

import controllers.Controllers;
import controllers.UtentiController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Utenti;

import java.io.IOException;
import java.io.PrintWriter;

public class GestioneServlet{
    private GestioneServlet(){}

    public static void inviaRisposta(HttpServletResponse response,int code,String message,boolean check) throws IOException{
        String text = "{\"message\":"+message+",\"check\":"+check+"}";
        PrintWriter writer = response.getWriter();
        //response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Origin","https://localhost:4200");
        response.addHeader("Access-Control-Allow-Methods","POST,GET,PUT,DELETE,HEAD,OPTIONS,TRACE");
        response.addHeader("Access-Control-Allow-Headers","X-CUSTOM,Content-Length,Content-Type,Authorization");
        response.addHeader("Access-Control-Max-Age","86400");
        response.setContentLength(text.length());
        response.setContentType("application/json");
        response.setStatus(code);
        writer.println(text);
        writer.flush();
        writer.close();
    }

    public static boolean isLogged(HttpServletRequest request,HttpServletResponse response) throws  IOException{
        String token = request.getHeader("Authorization");
        if (token==null){
            return false;
        }
        String[] auth = token.split(" ");
        if (auth.length<2){
            return false;
        }
        return true;
    }

    public static String extractEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{

        String token = request.getHeader("Authorization");
        if (token==null){
            return "";
        }
        String[] auth = token.split(" ");
        if (auth.length<2){
            return "";
        }
        String auth1 = auth[1];
        String email = GestioneToken.validateToken(auth1);
        if (email.equals("")){
            return "";
        }
        return email;
    }

    public static String validaToken(HttpServletRequest request,HttpServletResponse response) throws IOException{

        String token = request.getHeader("Authorization");
        if (token==null){
            GestioneServlet.inviaRisposta(response,403,"\"Token Non valido\"",false);
            return "";
        }
        String[] auth = token.split(" ");
        if (auth.length<2){
            GestioneServlet.inviaRisposta(response,403,"\"Token Incorrect\"",false);
            return "";
        }
        String auth1 = auth[1];
        String email = GestioneToken.validateToken(auth1);
        if (email.equals("")){
            GestioneServlet.inviaRisposta(response,403,"\"Token Incorrect\"",false);
            return "";
        }
        return email;
    }

    public static String controllaRuolo(String email) {
        UtentiController controller = new UtentiController();
        if (controller.checkAdmin(email)){
            return "admin";
        }
        return "cliente";
    }
}