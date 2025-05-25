package gmailManager.models;

import models.Oggetti;
import org.json.JSONObject;
import utility.Database;
import utility.DateManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

public class EmailMessage implements Oggetti<EmailMessage> {

    private final int id;
    private final String googleId;
    private final String threadId;
    private final String mittente;
    private final String destinatario;
    private final String oggetto;
    private final String testo;
    private final Calendar dataOraInvio;

    public EmailMessage() {
        this(0,"","","","","","",new GregorianCalendar());
    }

    public EmailMessage(int id, String googleId, String threadId, String mittente, String destinatario, String oggetto, String testo, Calendar dataOraInvio) {
        this.id = id;
        this.googleId = googleId;
        this.threadId = threadId;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.oggetto = oggetto;
        this.testo = testo;
        this.dataOraInvio = dataOraInvio;
    }

    public int getId() {
        return id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getMittente() {
        return mittente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public Calendar getDataOraInvio() {
        return dataOraInvio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailMessage that = (EmailMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Optional<EmailMessage> convertDBToJava(ResultSet rs) throws SQLException {
        return Optional.empty();
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("google_id",googleId);
        output.put("thread_id",threadId);
        output.put("mittente",mittente);
        output.put("destinatario",destinatario);
        output.put("oggetto",oggetto);
        output.put("testo",testo);
        output.put("data_ora_invio", DateManagement.printJSONCalendar(dataOraInvio));
        return output.toString(4);
    }
}
