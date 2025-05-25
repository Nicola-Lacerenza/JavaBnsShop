package bnsshop.bnsshop;

import controllers.Controllers;
import gmailManager.controllers.GmailTokenController;
import gmailManager.models.GmailToken;
import gmailManager.utility.ReadLoginData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "TokenGoogleServlet", value = "/TokenGoogleServlet")
public class TokenGoogleServlet extends HttpServlet {

    private Controllers<GmailToken> controller;
    @Override
    public void init() throws ServletException {
        super.init();
        controller = new GmailTokenController();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        JSONObject object = new JSONObject(builder.toString());
        String code = object.getString("code");

        Optional<GmailToken> request1 = ReadLoginData.creaGmailToken(code);
        if (request1.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore durante la creazione del token\"",false,false);
        }else {
            GestioneServlet.inviaRisposta(request,response,200,"\"Login Google Effettuato Correttamente\"",false,false);
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        Optional<GmailToken> token = ReadLoginData.getValidToken();
        if (token.isEmpty()){
            GestioneServlet.inviaRisposta(request,response,500,"\"Errore Durante il recupero del Token\"",false,false);
        }else{
            GestioneServlet.inviaRisposta(request,response,200,token.get().toString(),true,false);
        }


    }

}
