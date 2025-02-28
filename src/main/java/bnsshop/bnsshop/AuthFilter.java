package bnsshop.bnsshop;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utility.GestioneServlet;

import java.io.IOException;

@WebFilter("/ProdottiServlet")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)(request);
        HttpServletResponse httpResponse = (HttpServletResponse)(response);
        String method = httpRequest.getMethod().toLowerCase();
        String url = httpRequest.getServletPath();
        if (method.equals("option")){
            chain.doFilter(request, response);
            return;
        }
        if (url.equals("/LoginServlet") || url.equals("/RegisterServlet")){
            chain.doFilter(request, response);
            return;
        }

        String email = GestioneServlet.validaToken(httpRequest, httpResponse);
        if (email.isEmpty()) {
            GestioneServlet.inviaRisposta(httpResponse,401,"\"Email non valida!\"",false);
            return;
        }
        if (!controllaRuolo(httpRequest,email)){
            GestioneServlet.inviaRisposta(httpResponse, 403, "\"Ruolo non corretto!\"", false);
            return;
        }
        chain.doFilter(request,response);
    }

    private boolean controllaRuolo(HttpServletRequest request,String email){
        String method = request.getMethod().toLowerCase();
        if (method.equals("get")){
            return true;
        }
        if (method.equals("post") || method.equals("delete") || method.equals("put")){
            return "admin".equals(GestioneServlet.controllaRuolo(email));
        }
        return false;
    }
}
