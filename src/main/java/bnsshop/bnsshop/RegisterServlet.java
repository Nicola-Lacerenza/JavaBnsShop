package bnsshop.bnsshop;

import controllers.UtentiController;
import io.jsonwebtoken.security.Request;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.Crittografia;
import utility.GestioneServlet;
import utility.QueryFields;
import utility.TipoVariabile;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
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
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        if(GestioneServlet.aggiungiCorsSicurezzaHeadersDynamicPage(request,response)){
            System.out.println("CORS and security headers added correctly in the response.");
        }else{
            System.err.println("Error writing the CORS and security headers in the response.");
        }
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
        int idTagliaPreferita= object.getInt("id_taglia_preferita");
        int idColorePreferito = object.getInt("id_colore_preferito");
        String ruolo= "cliente";
        Optional<String> passwordHashed= Crittografia.get_SHA_512_SecurePassword(password,"1234");
        if(controller.checkEmail(email)){
            String registrazione = "\"L'email esiste!\"";
            GestioneServlet.inviaRisposta(request,response,400,registrazione,false,false);
            return;
        }
        if(passwordHashed.isPresent()){
            Map<Integer, QueryFields<? extends Comparable<?>>> request0= new HashMap<>();
            try{
                request0.put(0,new QueryFields<>("nome",nome,TipoVariabile.string));
                request0.put(1,new QueryFields<>("cognome",cognome,TipoVariabile.string));
                request0.put(2,new QueryFields<>("data_nascita",dataNascita,TipoVariabile.string));
                request0.put(3,new QueryFields<>("luogo_nascita",luogoNascita,TipoVariabile.string));
                request0.put(4,new QueryFields<>("sesso",sesso,TipoVariabile.string));
                request0.put(5,new QueryFields<>("email",email,TipoVariabile.string));
                request0.put(6,new QueryFields<>("telefono",telefono,TipoVariabile.string));
                request0.put(7,new QueryFields<>("password",passwordHashed.get(),TipoVariabile.string));
                request0.put(8,new QueryFields<>("id_taglia_preferita",idTagliaPreferita,TipoVariabile.longNumber));
                request0.put(9,new QueryFields<>("id_colore_preferito",idColorePreferito,TipoVariabile.longNumber));
                request0.put(10,new QueryFields<>("ruolo",ruolo,TipoVariabile.string));
            }catch(SQLException exception){
                exception.printStackTrace();
                String message = "\"Errore durante la registrazione.\"";
                GestioneServlet.inviaRisposta(request,response,500,message,false,false);
                return;
            }
            int idRegistrazione = controller.insertObject(request0);
            if (idRegistrazione > 0) {
                //String registrazione = "\"Registrazione effettuata correttamente.\"";
                //GestioneServlet.inviaRisposta(request,response,201,registrazione,true,false);
                request.setAttribute("username_registrazione",email);
                request.setAttribute("password_registrazione",password);
                request.getServletContext().getRequestDispatcher("/LoginServlet").forward(request,response);
            }else{
                String message = "\"Errore durante la registrazione.\"";
                GestioneServlet.inviaRisposta(request,response,500,message,false,false);
            }
        }else{
            String message = "{\"message\":\"Errore durante la registrazione.\",\"check\":false}";
            GestioneServlet.inviaRisposta(request,response,500,message,false,false);
        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    }
}