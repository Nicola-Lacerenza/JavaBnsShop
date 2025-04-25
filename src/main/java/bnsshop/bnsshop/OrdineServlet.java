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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = readId(request);

        String email = GestioneServlet.validaToken(request, response);
        if (email == null) {
            return;
        }

        UtentiController controllerUtenti = new UtentiController();
        Optional<Utenti> utenti = controllerUtenti.getUserByEmail(email);
        if (utenti.isEmpty()) {
            GestioneServlet.inviaRisposta(response, 500, "{\"error\":\"Utente non trovato!\"}", false);
            return;
        }
        int idUtente = utenti.get().getId();

        if (id == -2) {
            OrdineController ordineCtrl = (OrdineController) controller;
            List<Ordine> ordini = ordineCtrl.getObjectByUserID(idUtente);
            GestioneServlet.inviaRisposta(response, 200, ordini.toString(), true);
            return;
        }

        if (id > 0) {
            Optional<Ordine> tmp = controller.getObject(id);

            if (tmp.isEmpty()) {
                GestioneServlet.inviaRisposta(response, 404, "{\"error\":\"Ordine con id=" + id + " non trovato\"}", true);
                return;
            }

            Ordine ordine = tmp.get();
            if (ordine.getIdUtente() != idUtente) {
                GestioneServlet.inviaRisposta(response, 403, "{\"error\":\"Accesso negato\"}", true);
                return;
            }

            String jsonOrdine = ordine.toString();
            System.out.println("JSON inviato al client: " + jsonOrdine);
            GestioneServlet.inviaRisposta(response, 200, jsonOrdine, true);
            return;
        }
        GestioneServlet.inviaRisposta(response, 400, "{\"error\":\"Parametro id non valido\"}", true);
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
