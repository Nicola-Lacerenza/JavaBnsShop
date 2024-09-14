package utility;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GestioneServlet{
    private GestioneServlet(){}

    public static void inviaRisposta(HttpServletResponse response,int code,String message,boolean check) throws IOException{
        String text = "{\"message\":"+message+",\"check\":"+check+"}";
        PrintWriter writer = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST,GET,PUT,DELETE,HEAD,OPTIONS,TRACE");
        response.addHeader("Access-Control-Allow-Headers","X-CUSTOM,Content-Length,Content-Type");
        response.addHeader("Access-Control-Max-Age","86400");
        response.setContentLength(text.length());
        response.setContentType("application/json");
        response.setStatus(code);
        writer.println(text);
        writer.flush();
        writer.close();
    }
}