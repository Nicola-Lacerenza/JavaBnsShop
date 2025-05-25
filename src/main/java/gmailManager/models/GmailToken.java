package gmailManager.models;

import models.Oggetti;
import org.json.JSONObject;
import utility.DateManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

public class GmailToken implements Oggetti<GmailToken> , Comparable<GmailToken> {

    private final int id;
    private final String token;
    private final int scadenzaToken;
    private final String refreshToken;
    private final int scadenzaRefreshToken;
    private final String scope;
    private final String tipoToken;
    private final Calendar timestampCreazione;

    public GmailToken() {
        this(0,"",0,"",0,"","",new GregorianCalendar());
    }

    public GmailToken(int id, String token, int scadenzaToken, String refreshToken, int scadenzaRefreshToken, String scope, String tipoToken, Calendar timestampCreazione) {
        this.id = id;
        this.token = token;
        this.scadenzaToken = scadenzaToken;
        this.refreshToken = refreshToken;
        this.scadenzaRefreshToken = scadenzaRefreshToken;
        this.scope = scope;
        this.tipoToken = tipoToken;
        this.timestampCreazione = timestampCreazione;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public int getScadenzaToken() {
        return scadenzaToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getScadenzaRefreshToken() {
        return scadenzaRefreshToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public Calendar getTimestampCreazione() {
        return timestampCreazione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GmailToken that = (GmailToken) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("token",token);
        output.put("scadenza_token",scadenzaToken);
        output.put("refresh_token",refreshToken);
        output.put("scadenza_refresh_token",scadenzaRefreshToken);
        output.put("scope",scope);
        output.put("tipo_token",tipoToken);
        output.put("timestamp_creazione", DateManagement.printJSONCalendar(timestampCreazione));
        return output.toString(4);
    }

    @Override
    public Optional<GmailToken> convertDBToJava(ResultSet rs) throws SQLException {
        int id1 = rs.getInt("id");
        String token1 = rs.getString("token");
        int scadenzaToken1 = rs.getInt("scadenza_token");
        String refreshToken1 = rs.getString("refresh_token");
        int scadenzaRefreshToken1 = rs.getInt("scadenza_refresh_token");
        String scope1 = rs.getString("scope");
        String tipoToken1 = rs.getString("tipo_token");
        Timestamp timestampCreazione1 = rs.getTimestamp("timestamp_creazione");
        Calendar timestampCreazione2 = DateManagement.fromDatabaseToCalendar(timestampCreazione1);
        GmailToken output = new GmailToken(id1,token1,scadenzaToken1,refreshToken1,scadenzaRefreshToken1,scope1,tipoToken1,timestampCreazione2);
        return Optional.of(output);
    }

    @Override
    public int compareTo(GmailToken o) {
        if (o==null){
            return -1;
        }
        if (this.equals(o)){
            return 0;
        }
        return id-o.id;
    }
}
