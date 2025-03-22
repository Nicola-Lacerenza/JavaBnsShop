package bnsshop.bnsshop;

import controllers.ColoreController;
import controllers.ProdottiController;
import controllers.TagliaController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import models.Colore;
import models.ProdottiFull;
import models.Taglia;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utility.GestioneServlet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "ProdottiServlet", value = "/ProdottiServlet")
@MultipartConfig(maxFileSize = 16177215)
public class ProdottiServlet extends HttpServlet{
    ProdottiController controller;

    @Override
    public void init() throws ServletException{
        super.init();
        controller = new ProdottiController();
    }

    // Gestione richiesta preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type, Content-Length,Authorization");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{

        int id = readId(request);

        if (id == -2) {
            List<ProdottiFull> prodotti = this.controller.getAllProducts();
            List<ProdottiFull> prodotti2 = new LinkedList<>();
            for (int k=0 ; k<prodotti.size(); k++){
                ProdottiFull prodotto = prodotti.get(k);
                if(!prodotti2.contains(prodotto)){
                    prodotti2.add(prodotto);
                }
                List<ProdottiFull> prodottoFiltrato = prodotti.stream().filter(p -> p.getId()==prodotto.getId()).toList();
                int k1 = prodotti2.indexOf(prodotto);
                for (ProdottiFull prodottoFiltrato1 : prodottoFiltrato){
                    if (!prodotti2.get(k1).getUrl().containsAll(prodottoFiltrato1.getUrl())) {
                        prodotti2.get(k1).getUrl().addAll(prodottoFiltrato1.getUrl());
                    }
                    if (!prodotti2.get(k1).getTaglieProdotto().containsAll(prodottoFiltrato1.getTaglieProdotto())) {
                        prodotti2.get(k1).getTaglieProdotto().addAll(prodottoFiltrato1.getTaglieProdotto());
                    }
                    if (!prodotti2.get(k1).getNomeColore().containsAll(prodottoFiltrato1.getNomeColore())) {
                        prodotti2.get(k1).getNomeColore().addAll(prodottoFiltrato1.getNomeColore());
                    }
                }
            }
            GestioneServlet.inviaRisposta(response, 200, prodotti2.toString(), true);
            return;
        }


        if (id>0) {
            List<ProdottiFull> tmp = this.controller.getFullObject(id);
            ProdottiFull prodotto1 = new ProdottiFull(tmp.getFirst().getId(),tmp.getFirst().getIdModello(), tmp.getFirst().getNomeModello(), tmp.getFirst().getDescrizioneModello(), tmp.getFirst().getIdCategoria(),tmp.getFirst().getNomeCategoria(),tmp.getFirst().getTarget(), tmp.getFirst().getIdBrand()
                    ,tmp.getFirst().getNomeBrand(), tmp.getFirst().getDescrizioneBrand(), tmp.getFirst().getStatoPubblicazione(), tmp.getFirst().getPrezzo(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
            for (ProdottiFull tmp1 : tmp) {
                if (!prodotto1.getTaglieProdotto().containsAll(tmp1.getTaglieProdotto())){
                    prodotto1.getTaglieProdotto().addAll(tmp1.getTaglieProdotto());
                }
                if (!prodotto1.getUrl().containsAll(tmp1.getUrl())) {
                    prodotto1.getUrl().addAll(tmp1.getUrl());
                }
                if (!prodotto1.getNomeColore().containsAll(tmp1.getNomeColore())) {
                    prodotto1.getNomeColore().addAll(tmp1.getNomeColore());
                }
            }
            GestioneServlet.inviaRisposta(response,200,prodotto1.toString(),true);
        }else{

            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Map per memorizzare i dati di input non-file
        Map<String, String> formData = new HashMap<>();

        // Lista per salvare gli URL dei file caricati
        List<String> urls = new ArrayList<>();

        // Gestione delle parti della richiesta
        for (Part part : request.getParts()) {
            if (part.getContentType() == null) {
                // Parte non-file (probabilmente un campo di form)
                String fieldName = part.getName();
                String fieldValue = new BufferedReader(new InputStreamReader(part.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                formData.put(fieldName, fieldValue);

            } else {
                // Gestione dei file
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();

                // Estrai l'estensione dal nome originale
                String extension = "";
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    extension = fileName.substring(dotIndex);
                }

                // Genera un nome univoco usando UUID e aggiungi l'estensione
                String uniqueFileName = UUID.randomUUID().toString() + extension;

                // Specifica la directory completa dove vuoi salvare le immagini
                String directory = "C:\\Users\\nicol\\Documents\\PROGETTI\\BNS SHOP\\JAVA - INTELLIJ\\src\\main\\webapp\\images";
                //String directory = "C:\\Users\\Emanuele Schino\\Desktop\\PERSONALE\\JAVA\\src\\main\\webapp\\images";

                // Assicurati che la directory esista
                Path dirPath = Paths.get(directory);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath); // Crea la directory se non esiste
                }

                // Salva il file nella directory specificata
                Path filePath = Paths.get(directory, uniqueFileName );
                Files.copy(part.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Aggiungi l'URL del file alla lista
                String fileUrl = "images/" + uniqueFileName;  // Path relativo o URL come preferisci gestirlo
                urls.add(fileUrl);
            }
        }

        // Recupera i valori dal form
        String idCategoria= formData.get("id_categoria");
        String idBrand= formData.get("id_brand");
        String colori= formData.get("colori");
        String nome= formData.get("nome");
        String descrizione= formData.get("descrizione");
        Integer prezzo = Integer.parseInt(formData.get("prezzo"));
        Boolean statoPubblicazione = Boolean.parseBoolean(formData.get("stato_pubblicazione"));
        int statoPubblicazioneInt = statoPubblicazione ? 1 : 0;
        String taglie = formData.get("taglie");

        if (taglie.isEmpty()) {
            GestioneServlet.inviaRisposta(response, 500, "\"Errore durante la registrazione.\"", false);
            return;
        }

        JSONArray taglie1;
        try {
            taglie1 = new JSONArray(taglie);
        }catch (JSONException e){
            GestioneServlet.inviaRisposta(response,500,"\"Array delle taglie non corretto\"",false);
            return;
        }

        // Estrazione ID Taglia
        TagliaController tagliaController = new TagliaController();
        List<Taglia> listTaglia = tagliaController.getAllObjects();
        List<Double> numeriTaglie = new LinkedList<>();
        for (int i = 0;i<taglie1.length();i++){
            JSONObject tmp = (JSONObject) taglie1.get(i);
            numeriTaglie.add(tmp.getDouble("taglia"));
        }

        List<Integer> idTaglie = listTaglia.stream().filter(t -> numeriTaglie.contains(Double.parseDouble(t.getTagliaEu()))).map(t -> t.getId()).toList();

        // Estrazione degli ID Colore
        ColoreController coloreController = new ColoreController();
        List<Colore> listColore = coloreController.getAllObjects();

        // Se la stringa dei colori contiene più colori separati da una virgola
        String[] coloriArray = colori.split(",");

        // Lista per contenere gli ID dei colori
        List<Integer> idColori = new ArrayList<>();

        // Itera su tutti i colori passati nella stringa
        for (String coloreString : coloriArray) {
            String coloreTrimmed = coloreString.trim();  // Rimuovi eventuali spazi bianchi
            List<Integer> coloriTrovati = listColore.stream()
                    .filter(colore1 -> colore1.getNome().equalsIgnoreCase(coloreTrimmed)) // Usa equalsIgnoreCase per un confronto più robusto
                    .map(Colore::getId)
                    .collect(Collectors.toList());

            if (!coloriTrovati.isEmpty()) {
                idColori.addAll(coloriTrovati); // Aggiungi tutti gli ID trovati
            } else {
                // Gestione errore se un colore non viene trovato
                String message = "\"Errore: Colore " + coloreTrimmed + " non trovato.\"";
                GestioneServlet.inviaRisposta(response, 500, message, false);
                return;
            }
        }

        if (idColori.isEmpty()) {
            String message = "\"Errore durante la registrazione: nessun colore valido trovato.\"";
            GestioneServlet.inviaRisposta(response, 500, message, false);
            return;
        }

        // Preparazione dei dati per l'inserimento
        Map<Integer, RegisterServlet.RegisterFields> request0 = new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(1,new RegisterServlet.RegisterFields("id_categoria",idCategoria));
        request0.put(2,new RegisterServlet.RegisterFields("id_brand",idBrand));
        request0.put(3,new RegisterServlet.RegisterFields("descrizione",descrizione));
        request0.put(4,new RegisterServlet.RegisterFields("prezzo", "" + prezzo));
        request0.put(5,new RegisterServlet.RegisterFields("stato_pubblicazione", "" + statoPubblicazioneInt));

        // Inserisci tutti i colori nella mappa
        int index = 6; // Punto di partenza dell'indice
        for (int i = 0; i < idColori.size(); i++) {
            request0.put(index++, new RegisterServlet.RegisterFields("colore_" + (i + 1), String.valueOf(idColori.get(i))));
        }

        // Inserisci tutti gli url nella mappa
        for (int j = 0; j < urls.size(); j++) {
            request0.put(index++, new RegisterServlet.RegisterFields("url" + j, urls.get(j)));
        }

        // Inserisci tutti gli url nella mappa
        for (int j = 0; j < idTaglie.size(); j++) {
            request0.put(index++, new RegisterServlet.RegisterFields("taglia_" + j, ""+idTaglie.get(j)));
        }

        for (int j = 0; j < taglie1.length(); j++) {
            JSONObject tmp = (JSONObject) taglie1.get(j);
            request0.put(index++, new RegisterServlet.RegisterFields("quantita_" + j, ""+tmp.getInt("quantita")));
        }


        // Inserimento nel database
        if (controller.insertObject(request0)) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(response, 201, registrazione, true);
        } else {
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response, 500, message, false);
            for (String url : urls){
                File file = new File(url);
                file.delete();
            }

        }
    }

    @Override
    public void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{

        // Estrazione dei campi non-file e dei file dalla request
        Map<String, String> formData = extractFormData(request);
        //List<String> imageUrls = processFileParts(request);
        String imageUrls1 = formData.get("url");
        JSONArray imageUrls = new JSONArray(imageUrls1);

        // Recupera i valori dal form
        Integer id = Integer.parseInt(formData.get("id"));
        String idCategoria= formData.get("id_categoria");
        String idBrand= formData.get("id_brand");
         String colori= formData.get("colori");
        String nome= formData.get("nome");
        String descrizione= formData.get("descrizione");
        Integer prezzo = Integer.parseInt(formData.get("prezzo"));
        Boolean statoPubblicazione = Boolean.parseBoolean(formData.get("stato_pubblicazione"));
        int statoPubblicazioneInt = statoPubblicazione ? 1 : 0;
        String taglie = formData.get("taglie");

        if (taglie.isEmpty()) {
            GestioneServlet.inviaRisposta(response, 500, "\"Errore durante la registrazione.\"", false);
            return;
        }

        JSONArray taglie1;
        try {
            taglie1 = new JSONArray(taglie);
        }catch (JSONException e){
            GestioneServlet.inviaRisposta(response,500,"\"Array delle taglie non corretto\"",false);
            return;
        }

        // Estrazione ID Taglia
        TagliaController tagliaController = new TagliaController();
        List<Taglia> listTaglia = tagliaController.getAllObjects();
        List<Double> numeriTaglie = new LinkedList<>();
        for (int i = 0;i<taglie1.length();i++){
            JSONObject tmp = (JSONObject) taglie1.get(i);
            numeriTaglie.add(tmp.getDouble("taglia"));
        }

        List<Integer> idTaglie = listTaglia.stream().filter(t -> numeriTaglie.contains(Double.parseDouble(t.getTagliaEu()))).map(t -> t.getId()).toList();

        // Estrazione degli ID Colore
        ColoreController coloreController = new ColoreController();
        List<Colore> listColore = coloreController.getAllObjects();

        // Se la stringa dei colori contiene più colori separati da una virgola
        String[] coloriArray = colori.split(",");

        // Lista per contenere gli ID dei colori
        List<Integer> idColori = new ArrayList<>();

        // Itera su tutti i colori passati nella stringa
        for (String coloreString : coloriArray) {
            String coloreTrimmed = coloreString.trim();  // Rimuovi eventuali spazi bianchi
            List<Integer> coloriTrovati = listColore.stream()
                    .filter(colore1 -> colore1.getNome().equalsIgnoreCase(coloreTrimmed)) // Usa equalsIgnoreCase per un confronto più robusto
                    .map(Colore::getId)
                    .collect(Collectors.toList());

            if (!coloriTrovati.isEmpty()) {
                idColori.addAll(coloriTrovati); // Aggiungi tutti gli ID trovati
            } else {
                // Gestione errore se un colore non viene trovato
                String message = "\"Errore: Colore " + coloreTrimmed + " non trovato.\"";
                GestioneServlet.inviaRisposta(response, 500, message, false);
                return;
            }
        }

        if (idColori.isEmpty()) {
            String message = "\"Errore durante la registrazione: nessun colore valido trovato.\"";
            GestioneServlet.inviaRisposta(response, 500, message, false);
            return;
        }

        // Preparazione dei dati per l'inserimento
        Map<Integer, RegisterServlet.RegisterFields> request0 = new HashMap<>();
        request0.put(0,new RegisterServlet.RegisterFields("nome",nome));
        request0.put(1,new RegisterServlet.RegisterFields("id_categoria",idCategoria));
        request0.put(2,new RegisterServlet.RegisterFields("id_brand",idBrand));
        request0.put(3,new RegisterServlet.RegisterFields("descrizione",descrizione));
        request0.put(4,new RegisterServlet.RegisterFields("prezzo", "" + prezzo));
        request0.put(5,new RegisterServlet.RegisterFields("stato_pubblicazione", "" + statoPubblicazioneInt));

        // Inserisci tutti i colori nella mappa
        int index = 6; // Punto di partenza dell'indice
        for (int i = 0; i < idColori.size(); i++) {
            request0.put(index++, new RegisterServlet.RegisterFields("colore_" + (i + 1), String.valueOf(idColori.get(i))));
        }

        // Inserisci tutti gli url nella mappa
        for (int j = 0; j < imageUrls.length(); j++) {
            request0.put(index++, new RegisterServlet.RegisterFields("url" + j, (String)imageUrls.get(j)));
        }

        // Inserisci tutti gli url nella mappa
        for (int j = 0; j < idTaglie.size(); j++) {
            request0.put(index++, new RegisterServlet.RegisterFields("taglia_" + j, ""+idTaglie.get(j)));
        }

        for (int j = 0; j < taglie1.length(); j++) {
            JSONObject tmp = (JSONObject) taglie1.get(j);
            request0.put(index++, new RegisterServlet.RegisterFields("quantita_" + j, ""+tmp.getInt("quantita")));
        }


        // Inserimento nel database
        if (controller.updateObject(id,request0)) {
            String registrazione = "\"Registrazione effettuata correttamente.\"";
            GestioneServlet.inviaRisposta(response, 201, registrazione, true);
        } else {
            String message = "\"Errore durante la registrazione.\"";
            GestioneServlet.inviaRisposta(response, 500, message, false);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{

        int id= Integer.parseInt((String) request.getParameter("id"));
        if (this.controller.deleteObject(id)){
            String message = "\"Product deleted Correctly.\"";
            GestioneServlet.inviaRisposta(response,200,message,true);
        }else{
            String message = "\"Internal server error\"";
            GestioneServlet.inviaRisposta(response,500,message,false);
        }
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

    private Map<String, String> extractFormData(HttpServletRequest request) throws IOException, ServletException {
        Map<String, String> formData = new HashMap<>();
        for (Part part : request.getParts()) {
            if (part.getContentType() == null) {
                String fieldName = part.getName();
                String fieldValue = new BufferedReader(new InputStreamReader(part.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                formData.put(fieldName, fieldValue);
            }
        }
        return formData;
    }

    private List<String> processFileParts(HttpServletRequest request) throws IOException, ServletException {
        List<String> urls = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getContentType() != null) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                // Specifica la directory dove salvare le immagini
                String directory = "C:\\Users\\nicol\\Documents\\PROGETTI\\BNS SHOP\\JAVA - INTELLIJ\\src\\main\\webapp\\images";
                File file = new File(directory + "\\" + fileName);
                if (!file.exists()) {
                    Path dirPath = Paths.get(directory);
                    if (!Files.exists(dirPath)) {
                        Files.createDirectories(dirPath);
                    }
                    Path filePath = Paths.get(directory, fileName);
                    Files.copy(part.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    // Aggiunge l'URL relativo alla lista
                    urls.add("images/" + fileName);
                }
            }
        }
        return urls;
    }
}
