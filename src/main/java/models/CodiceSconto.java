package models;

import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class CodiceSconto implements Oggetti<CodiceSconto>{
    private final int id;
    private final String codice;
    private final int valore;
    private final String descrizione;
    private final String tipo;
    private final Calendar dataInizio;
    private final Calendar dataFine;
    private final int usoMassimo;
    private final int usoPerUtente;
    private final int minimoAcquisto;
    private final int attivo;

    public CodiceSconto(int id, String codice, int valore , String descrizione , String tipo , Calendar dataInizio, Calendar dataFine , int usoMassimo,int usoPerUtente,int minimoAcquisto,int attivo) {
        this.id = id;
        this.codice = codice;
        this.valore = valore;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.usoMassimo = usoMassimo;
        this.usoPerUtente = usoPerUtente;
        this.minimoAcquisto = minimoAcquisto;
        this.attivo = attivo;
    }

    public CodiceSconto(){
        this(0,"",0,"","",Calendar.getInstance(),Calendar.getInstance(),0,0,0,0);
    }

    public int getId() {
        return id;
    }

    public String getCodice() {
        return codice;
    }

    public double getValore() {
        return valore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public Calendar getDataInizio() {
        return dataInizio;
    }

    public Calendar getDataFine() {
        return dataFine;
    }

    public int getUsoMassimo() {
        return usoMassimo;
    }

    public int getUsoPerUtente() {
        return usoPerUtente;
    }

    public double getMinimoAcquisto() {
        return minimoAcquisto;
    }

    public int getAttivo() {
        return attivo;
    }

    @Override
    public Optional<CodiceSconto> convertDBToJava(ResultSet rs) {
        try{
            int id1= rs.getInt("id");
            String codice = rs.getString("codice");
            int valore = rs.getInt("valore");
            String descrizione = rs.getString("descrizione");
            String tipo = rs.getString("tipo");
            // Gestione data_inizio
            Calendar dataInizio = null;
            java.sql.Date dataInizioSql = rs.getDate("data_inizio");
            if (dataInizioSql != null) {
                dataInizio = new GregorianCalendar();
                dataInizio.setTime(dataInizioSql);
            }

            // Gestione data_fine
            Calendar dataFine = null;
            java.sql.Date dataFineSql = rs.getDate("data_fine");
            if (dataFineSql != null) {
                dataFine = new GregorianCalendar();
                dataFine.setTime(dataFineSql);
            }
            int usoMassimo= rs.getInt("uso_massimo");
            int usoPerUtente= rs.getInt("uso_per_utente");
            int minimoAcquisto = rs.getInt("minimo_acquisto");
            int attivo= rs.getInt("attivo");
            return Optional.of(new CodiceSconto(id1,codice,valore,descrizione,tipo,dataInizio,dataFine,usoMassimo,usoPerUtente,minimoAcquisto,attivo));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("codice",codice);
        output.put("valore",valore);
        output.put("descrizione",descrizione);
        output.put("tipo",tipo);
        output.put("data_inizio",dataInizio);
        output.put("data_fine",dataFine);
        output.put("uso_massimo",usoMassimo);
        output.put("uso_per_utente",usoPerUtente);
        output.put("minimo_acquisto",minimoAcquisto);
        output.put("attivo",attivo);
        return output.toString(4);
    }
}
