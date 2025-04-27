package bnsshop.bnsshop;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utility.GestioneServlet;
import java.io.IOException;

@WebFilter("/*")
public class StaticPageFilter implements Filter{
    public StaticPageFilter(){}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest)(request);
        HttpServletResponse httpResponse = (HttpServletResponse)(response);
        String method = httpRequest.getMethod().toUpperCase();
        String path = httpRequest.getServletPath();
        if(method.equals("OPTIONS")){
            chain.doFilter(request,response);
            return;
        }
        if(path.endsWith(".jsp") || path.endsWith(".js") || path.endsWith(".css") ||
                path.endsWith(".ico") || path.endsWith(".png") || path.endsWith(".jpg") ||
                path.endsWith(".jpeg") || path.endsWith(".tiff") || path.endsWith(".webp") ||
                path.endsWith(".svg") || path.endsWith(".mp4")){
            if(!method.equals("GET") && !method.equals("POST")){
                GestioneServlet.inviaRisposta(httpRequest,httpResponse,403,"\"Unauthorized to proceed.\"",false,true);
                return;
            }
            if(GestioneServlet.aggiungiCorsSicurezzaHeadersStaticPage(httpRequest,httpResponse)){
                System.out.println("CORS and security headers added correctly in the response.");
                chain.doFilter(request,response);
                return;
            }else{
                System.err.println("Error writing the CORS and security headers in the response.");
                return;
            }
        }else{
            chain.doFilter(request,response);
        }
    }
}