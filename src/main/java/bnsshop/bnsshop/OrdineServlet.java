package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.OrdineController;
import controllers.ProdottiController;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.FornitoriProdotti;
import models.Ordine;
import models.Utenti;
import utility.GestioneServlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrdineServlet", value = "/OrdineServlet")
public class OrdineServlet extends HttpServlet {

    private Controllers<Ordine> controller;

    @Override
    public void init() throws ServletException {
        super.init();
        controller = new OrdineController();
    }

    // Gestione richiesta preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length,Authorization");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

        String email = GestioneServlet.validaToken(request,response);
        Controllers<Utenti> controllerUtenti = new UtentiController();
        List<Utenti> utenti = controllerUtenti.executeQuery("SELECT * FROM utenti WHERE email= '" + email + "'");
        if (utenti.isEmpty()){
            GestioneServlet.inviaRisposta(response,500,"\"Utente Non trovato!\"",false);
            return;
        }
        int idUtente = utenti.getFirst().getId();

        OrdineController specific = (OrdineController)(controller);
        List<Ordine> ordini = specific.getObjectByUserID(idUtente);
        GestioneServlet.inviaRisposta(response,200,ordini.toString(),true);

    }


}
