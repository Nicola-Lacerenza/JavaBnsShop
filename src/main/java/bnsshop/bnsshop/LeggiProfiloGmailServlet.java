package bnsshop.bnsshop;

import controllers.ModelloController;
import gmailManager.models.UserProfile;
import gmailManager.utility.UserReadProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utility.GestioneServlet;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ControllaRuoloServlet", value = "/ControllaRuoloServlet")
public class LeggiProfiloGmailServlet extends HttpServlet {

    public void init() throws ServletException{
        super.init();
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        String accessToken="";
        String userId="";
        UserReadProfile readProfile = new UserReadProfile(accessToken,userId);

        Optional<UserProfile> profile = readProfile.execute();
        if (profile.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore Interno al Server durante la lettura del profilo Gmail\"",false,false);
        }else{
            GestioneServlet.inviaRisposta(request,response,200,profile.get().toString(),true,false);
        }


    }
}
