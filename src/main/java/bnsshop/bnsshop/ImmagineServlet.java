package bnsshop.bnsshop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utility.GestioneServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "ImmagineServlet", value = "/ImmagineServlet")
public class ImmagineServlet extends HttpServlet {
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
      String nomeFile = request.getParameter("immagine");
      if (nomeFile == null || nomeFile.isEmpty()){
          GestioneServlet.inviaRisposta(request,response,400,"\"Richiesta non valida\"",false,false);
          return;
      }
        String directory = "C:\\Users\\nicol\\Documents\\PROGETTI\\BNS SHOP\\JAVA - INTELLIJ\\src\\main\\webapp\\images";

        Path percorsoFIle = Paths.get(directory,nomeFile).normalize();
        if (!Files.exists(percorsoFIle) || !Files.isReadable(percorsoFIle)){
            GestioneServlet.inviaRisposta(request,response,404,"\"File Non Trovato\"",false,false);
            return;
        }

        String tipoFile = Files.probeContentType(percorsoFIle);
        if (!ControllaTipoFile(tipoFile)){
            GestioneServlet.inviaRisposta(request,response,415,"\"Il tipo del File non valido\"",false,false);
            return;
        }

        if (GestioneServlet.aggiungiCorsSicurezzaHeadersDynamicPage(request,response)){
            System.out.println("header di sicurezza aggiunti");
        }else{
            System.err.println("header di sicurezza non aggiunti");
            return;
        }

        response.setContentType(tipoFile);
        response.setStatus(200);

        try(OutputStream out=response.getOutputStream();
            InputStream in = Files.newInputStream(percorsoFIle)){

            byte[] buffer = new byte[1024];
            int byteLetto = in.read(buffer);
            while (byteLetto != -1){
                out.write(buffer,0,byteLetto);
                byteLetto = in.read(buffer);
            }
        }

    }

    private boolean ControllaTipoFile(String tipoFile){
        if (tipoFile== null || tipoFile.isEmpty()){
            return false;
        }
        return tipoFile.equals("image/png")
                || tipoFile.equals("image/jpeg")
                || tipoFile.equals("image/jpg")
                || tipoFile.equals("image/tiff")
                || tipoFile.equals("image/webp")
                || tipoFile.equals("image/avif")
                || tipoFile.equals("image/svg+xml")
                || tipoFile.equals("video/mp4");
    }
}
