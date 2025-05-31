package bnsshop.bnsshop;

import controllers.Controllers;
import controllers.OrdineController;
import controllers.UtentiController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = readId(request);

        String email = GestioneServlet.validaToken(request, response);
        if (email == null) {
            return;
        }

        UtentiController controllerUtenti = new UtentiController();
        Optional<Utenti> utenti = controllerUtenti.getUserByEmail(email);
        if (utenti.isEmpty()) {
            GestioneServlet.inviaRisposta(request,response, 500, "{\"error\":\"Utente non trovato!\"}", false,false);
            return;
        }
        int idUtente = utenti.get().getId();
        String ruolo = utenti.get().getRuolo();


        if (id == -2) {
            OrdineController ordineCtrl = (OrdineController) controller;
            List<Ordine> ordini = ordineCtrl.getObjectByUserID(idUtente);
            GestioneServlet.inviaRisposta(request,response, 200, ordini.toString(), true,false);
            return;
        }

        if (id > 0) {

            Optional<Ordine> tmp;
            if (ruolo.equals("admin")){
                OrdineController specific = (OrdineController)(controller);
                tmp = specific.getObjectForAdmin(id);
            }else {
                tmp = controller.getObject(id);
            }

            if (tmp.isEmpty()) {
                GestioneServlet.inviaRisposta(request,response, 404, "{\"error\":\"Ordine con id=" + id + " non trovato\"}", true,false);
                return;
            }

            Ordine ordine = tmp.get();
            if(!ruolo.equals("admin")){
                if (ordine.getIdUtente() != idUtente) {
                    GestioneServlet.inviaRisposta(request,response, 403, "{\"error\":\"Accesso negato\"}", true,false);
                    return;
                }
            }
            String jsonOrdine = ordine.toString();
            GestioneServlet.inviaRisposta(request,response, 200, jsonOrdine, true,false);
            return;
        }
        GestioneServlet.inviaRisposta(request,response, 400, "{\"error\":\"Parametro id non valido\"}", true,false);
    }


    private int readId(HttpServletRequest request){
        String string = request.getParameter("id");
        if(string==null){
            //Entro qui se l'id è nullo
            return -2;
        }
        int id;
        try{
            id = Integer.parseInt(string);
        }catch (NumberFormatException e){
            //Vado in errore con codice di errore -1 se il parametro id è presente ma non è un numero valido
            e.printStackTrace();
            id = -1;
        }
        return id;
    }
}
