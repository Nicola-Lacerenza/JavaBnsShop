package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public class Pagamento implements Oggetti<Pagamento>{
    private final int id;
    private final String nomeTitolare;
    private final String numeroCarta;
    private final Calendar dataScadenza;
    private final String codiceVerifica;
    private final int idCustomers;

    public Pagamento(){
        this(0,"","",Calendar.getInstance(),"",0);
    }

    public Pagamento(int id, String nomeTitolare, String numeroCarta, Calendar dataScadenza, String codiceVerifica, int idCustomers) {
        this.id = id;
        this.nomeTitolare = nomeTitolare;
        this.numeroCarta = numeroCarta;
        this.dataScadenza = dataScadenza;
        this.codiceVerifica = codiceVerifica;
        this.idCustomers = idCustomers;
    }

    public int getId() {
        return id;
    }

    public String getNomeTitolare() {
        return nomeTitolare;
    }

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public Calendar getDataScadenza() {
        return dataScadenza;
    }

    public String getCodiceVerifica() {
        return codiceVerifica;
    }

    public int getIdCustomers() {
        return idCustomers;
    }

    @Override
    public Optional<Pagamento> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nomeTitolare1 = rs.getString("nome_titolare");
            String numeroCarta1 = rs.getString("numero_carta");
            Calendar dataScadenza1 = new GregorianCalendar();
            dataScadenza1.setTime(rs.getDate("data_scadenza"));
            String codiceVerifica1 = rs.getString("codice_verifica");
            int idCustomers1 = rs.getInt("id_customers");
            return Optional.of(new Pagamento(id1,nomeTitolare1,numeroCarta1,dataScadenza1,codiceVerifica1,idCustomers1));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("id",id);
        output.put("nome_titolare",nomeTitolare);
        output.put("numero_carta",numeroCarta);
        output.put("data_scadenza",dataScadenza);
        output.put("codice_verifica",codiceVerifica);
        output.put("id_customers",idCustomers);
        return output.toString(4);
    }
}