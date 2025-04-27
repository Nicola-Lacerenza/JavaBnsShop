package utility;

import controllers.UtentiController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Predicate;

public class GestioneServlet{
    private static final String PROJECT_NAME = "bnsshop";
    private static final String CLIENT_URL = "https://localhost:4200";
    private static final String PRODUCTION_SERVER_URL = "https://localhost:8444";
    private static final String DEVELOPMENT_SERVER_URL = "https://localhost:8444/" + PROJECT_NAME;

    private GestioneServlet(){}

    public static void inviaRisposta(HttpServletRequest request,HttpServletResponse response,int code,String message,boolean check,boolean staticPage) throws IOException{
        String text = "{\"message\":"+message+",\"check\":"+check+"}";
        PrintWriter writer = response.getWriter();
        if(aggiungiCorsSicurezzaHeaders(request,response,staticPage)){
            System.out.println("CORS and security headers added correctly.");
            response.setContentLength(text.length());
            response.setContentType("application/json");
            response.setStatus(code);
            writer.println(text);
            writer.flush();
            writer.close();
        }
        System.err.println("Error inserting CORS and security headers.");
    }

    public static boolean isLogged(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token==null){
            return false;
        }
        String[] auth = token.split(" ");
        return auth.length >= 2;
    }

    public static String extractEmail(HttpServletRequest request){
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
        if (email.isEmpty()){
            return "";
        }
        return email;
    }

    public static String validaToken(HttpServletRequest request,HttpServletResponse response) throws IOException{

        String token = request.getHeader("Authorization");
        if (token==null){
            GestioneServlet.inviaRisposta(request,response,403,"\"Token Non valido\"",false,false);
            return "";
        }
        String[] auth = token.split(" ");
        if (auth.length<2){
            GestioneServlet.inviaRisposta(request,response,403,"\"Token Incorrect\"",false,false);
            return "";
        }
        String auth1 = auth[1];
        String email = GestioneToken.validateToken(auth1);
        if (email.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,403,"\"Token Incorrect\"",false,false);
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

    public static boolean aggiungiCorsSicurezzaHeadersStaticPage(HttpServletRequest request,HttpServletResponse response){
        if(request == null || response == null){
            return false;
        }
        return aggiungiCorsSicurezzaHeaders(request,response,true);
    }

    public static boolean aggiungiCorsSicurezzaHeadersDynamicPage(HttpServletRequest request,HttpServletResponse response){
        if(request == null || response == null){
            return false;
        }
        return aggiungiCorsSicurezzaHeaders(request,response,false);
    }

    private static boolean aggiungiCorsSicurezzaHeaders(HttpServletRequest request,HttpServletResponse response,boolean staticPage){
        if(request == null || response == null){
            return false;
        }
        String origin;
        if(!staticPage){
            Optional<String> originCalculated = controllaOriginRequest(request);
            if(originCalculated.isEmpty()){
                return false;
            }
            origin = originCalculated.get();
        }else{
            origin = PRODUCTION_SERVER_URL;
        }
        String contentSecurityPolicy = "default-src 'self';";
        contentSecurityPolicy += "connect-src 'self' https://checkout.stripe.com https://api.stripe.com https://maps.googleapis.com;";
        contentSecurityPolicy += "script-src 'self' https://checkout.stripe.com https://*.js.stripe.com https://js.stripe.com https://maps.googleapis.com;";
        contentSecurityPolicy += "style-src 'self' 'unsafe-inline';";
        contentSecurityPolicy += "img-src 'self' https://*.stripe.com;";
        contentSecurityPolicy += "frame-src 'self' https://checkout.stripe.com https://*.js.stripe.com https://js.stripe.com https://hooks.stripe.com;";
        contentSecurityPolicy += "frame-ancestors 'none';";
        contentSecurityPolicy += "base-uri 'self';";
        contentSecurityPolicy += "form-action 'self'";
        response.addHeader("Access-Control-Allow-Origin",origin);
        response.addHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,HEAD,PUT,DELETE,TRACE");
        response.addHeader("Access-Control-Max-Age","86400");
        response.addHeader("Access-Control-Allow-Headers","Content-Length,Content-Type,X-CSRF-Token,x-csrf-token,Authorization");
        response.addHeader("Access-Control-Allow-Credentials","true");
        response.addHeader("Cache-Control","no-store,must-revalidate");
        response.addHeader("Content-Security-Policy",contentSecurityPolicy);
        response.setHeader("X-Content-Type-Options","nosniff");
        response.addHeader("Referrer-Policy","strict-origin-when-cross-origin");
        response.addHeader("Permissions-Policy","geolocation=(), microphone=(), camera=()");
        response.addHeader("Cross-Origin-Resource-Policy","same-origin");
        if(origin.contains(".stripe.com")){
            response.addHeader("Cross-Origin-Opener-Policy","same-origin-allow-popups");
        }else{
            response.addHeader("Cross-Origin-Opener-Policy","same-origin");
            response.addHeader("Cross-Origin-Embedder-Policy","require-corp");
        }
        return true;
    }

    private static Optional<String> controllaOriginRequest(HttpServletRequest request){
        String[] allowedOrigins = {
                CLIENT_URL,
                CLIENT_URL + "/",
                PRODUCTION_SERVER_URL,
                PRODUCTION_SERVER_URL + "/",
                DEVELOPMENT_SERVER_URL,
                DEVELOPMENT_SERVER_URL + "/",
                "https://js.stripe.com",
                "https://js.stripe.com/",
                "https://checkout.stripe.com",
                "https://checkout.stripe.com/",
                "https://api.stripe.com",
                "https://api.stripe.com/",
                "https://hooks.stripe.com",
                "https://hooks.stripe.com/"
        };
        String origin = request.getHeader("Origin");
        if(origin == null){
            String referer = request.getHeader("Referer");
            if(referer == null){
                return Optional.empty();
            }
            if(controllaCondizione(allowedOrigins,host -> referer.startsWith(host))){
                return Optional.of(referer);
            }
            return Optional.empty();
        }
        if(controllaCondizione(allowedOrigins,host -> origin.equals(host))){
            return Optional.of(origin);
        }
        return Optional.empty();
    }

    private static boolean controllaCondizione(String[] allowedHosts,Predicate<String> condition){
        for(String allowedHost:allowedHosts){
            if(condition.test(allowedHost)){
                return true;
            }
        }
        return false;
    }
}