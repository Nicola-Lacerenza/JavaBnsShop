package gmailManager.utility;

import controllers.Controllers;
import gmailManager.controllers.EmailMessageController;
import gmailManager.models.EmailMessage;
import org.json.JSONObject;
import utility.GestioneFile;
import utility.QueryFields;
import utility.TipoVariabile;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class SendEmailRequest extends GmailAPIRequest<EmailMessage>{

    private final String mittente;

    private final String destinatario;

    private final String oggetto;

    private final String testo;

    private final String nomeFileAllegato;

    public SendEmailRequest(String accessToken,String mittente,String destinatario,String oggetto,String testo,String nomeFileAllegato) {
        super("https://gmail.googleapis.com/gmail/v1",accessToken);
        this.mittente=mittente;
        this.destinatario=destinatario;
        this.oggetto=oggetto;
        this.testo=testo;
        this.nomeFileAllegato=nomeFileAllegato;
    }

    @Override
    public String getEndpoint() {
        return "/users/"+mittente+"/messages/send";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer " + getAccessToken());
        headers.put("Content-Type","application/json");
        return headers;
    }

    @Override
    public String getBody() {
        String allegato = GestioneFile.leggiFilePDF(nomeFileAllegato);
        String mimeMessage =
                "MIME-Version: 1.0\r\n" +
                        "Content-Type: multipart/mixed; boundary=\"foo_bar_baz\"\r\n" +
                        "From: " + mittente + "\r\n" +
                        "To: " + destinatario + "\r\n" +
                        "Subject: " + oggetto + "\r\n" +
                        "foo_bar_baz\r\n" +
                        "Content-Type: text/plain; charset=utf-8\r\n" +
                        "\r\n" + testo +
                        "\r\n" +
                        "--foo_bar_baz\r\n" +
                        "Content-Type: application/pdf\r\n" +
                        "Content-Disposition: attachment; filename="+nomeFileAllegato+"\r\n" +
                        "Content-Transfer-Encoding: base64\r\n" +
                        "\r\n" +
                        allegato +
                        "\r\n" +
                        "--foo_bar_baz--";                ;
                        String rawEncoded = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(mimeMessage.getBytes(StandardCharsets.UTF_8));
        JSONObject json = new JSONObject();
        json.put("raw",rawEncoded);
        return json.toString(4);
    }

    @Override
    public Optional<EmailMessage> parseResponse(JSONObject jsonResponse) {
        Map<Integer, QueryFields<? extends Comparable<?>>> fields = new HashMap<>();
        try {
            fields.put(0,new QueryFields<>("google_id",jsonResponse.getString("id"), TipoVariabile.string));
            fields.put(1,new QueryFields<>("thread_id",jsonResponse.getString("threadId"), TipoVariabile.string));
            fields.put(2,new QueryFields<>("mittente",mittente, TipoVariabile.string));
            fields.put(3,new QueryFields<>("destinatario",destinatario, TipoVariabile.string));
            fields.put(4,new QueryFields<>("oggetto",oggetto, TipoVariabile.string));
            fields.put(5,new QueryFields<>("nome_file_allegato",nomeFileAllegato, TipoVariabile.string));
            fields.put(6,new QueryFields<>("testo",testo, TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
        Controllers<EmailMessage> controller = new EmailMessageController();
        int databaseid = controller.insertObject(fields);
        if (databaseid<=0){
            return Optional.empty();
        }
        EmailMessage output = new EmailMessage(databaseid,jsonResponse.getString("id"),jsonResponse.getString("threadId"),mittente,destinatario,oggetto,testo,nomeFileAllegato,new GregorianCalendar());
        return Optional.of(output);
    }
}
