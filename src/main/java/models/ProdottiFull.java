package models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProdottiFull implements Oggetti<ProdottiFull>{

    private final int id;
    private final int idModello;
    private final String nomeModello ;
    private final String descrizioneModello ;
    private final int idCategoria;
    private final String nomeCategoria;
    private final String target;
    private final int idBrand;
    private final String nomeBrand;
    private final String descrizioneBrand;
    private final int statoPubblicazione;
    private final double prezzo;
    private final List<ProdottiTaglieEstratte> taglieProdotto;
    private final List<String> url;
    private final List<String> nomeColore;

    public ProdottiFull(int id,int idModello, String nomeModello, String descrizioneModello,int idCategoria, String nomeCategoria, String target,int idBrand,String nomeBrand, String descrizioneBrand, int statoPubblicazione, double prezzo, /*List<String> tagliaEu, List<String> tagliaUk, List<String> tagliaUs,List<String> quantita,*/List<ProdottiTaglieEstratte> list, List<String> url, List<String> nomeColore) {
        this.id = id;
        this.idModello = idModello;
        this.nomeModello = nomeModello;
        this.descrizioneModello = descrizioneModello;
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.target = target;
        this.idBrand = idBrand;
        this.nomeBrand = nomeBrand;
        this.descrizioneBrand = descrizioneBrand;
        this.statoPubblicazione = statoPubblicazione;
        this.prezzo = prezzo;
        this.taglieProdotto = list;
        this.url = url;
        this.nomeColore = nomeColore;
    }
    public ProdottiFull() {
        this(0,0,"","",0,"","",0,"","",0,0,new LinkedList<>(),new LinkedList<>(),new LinkedList<>());
    }

    public int getId() {
        return id;
    }
    public int getIdModello() { return idModello;}

    public String getNomeModello() {
        return nomeModello;
    }

    public String getDescrizioneModello() {
        return descrizioneModello;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getTarget() {
        return target;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public String getNomeBrand() {
        return nomeBrand;
    }

    public String getDescrizioneBrand() {
        return descrizioneBrand;
    }

    public int getStatoPubblicazione() {
        return statoPubblicazione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public List<ProdottiTaglieEstratte> getTaglieProdotto() {
        return taglieProdotto;
    }

    public List<String> getUrl() {
        return url;
    }

    public List<String> getNomeColore() {
        return nomeColore;
    }

    @Override
    public Optional<ProdottiFull> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            int idModello1 = rs.getInt("id_modello");
            String nomeModello1 = rs.getString("nome_modello");
            String descrizioneModello1 = rs.getString("descrizione_modello");
            int idCategoria1 = rs.getInt("id_categoria");
            String nomeCategoria1 = rs.getString("nome_categoria");
            String target1 = rs.getString("target");
            int idBrand1 = rs.getInt("id_brand");
            String nomeBrand1 = rs.getString("nome_brand");
            String descrizioneBrand1 = rs.getString("descrizione_brand");
            int statoPubblicazione1 = rs.getInt("stato_pubblicazione");
            double prezzo1 = rs.getDouble("prezzo");
            String taglia_Eu1 = rs.getString("taglia_Eu");
            String taglia_Uk1 = rs.getString("taglia_Uk");
            String taglia_Us1 = rs.getString("taglia_Us");
            String quantita1 = rs.getString("quantita");
            String url1 = rs.getString("url");
            String nomeColore1 = rs.getString("nome_colore");
            List<ProdottiTaglieEstratte> tmp1 = new LinkedList<>();
            List<String> tmp2 = new LinkedList<>();
            List<String> tmp3 = new LinkedList<>();
            tmp1.add(new ProdottiTaglieEstratte(new Taglia(0,taglia_Eu1,taglia_Uk1,taglia_Us1),new TaglieProdotti(0,0,id1,Integer.parseInt(quantita1))));
            tmp2.add(url1);
            tmp3.add(nomeColore1);
            return Optional.of(new ProdottiFull(id1,idModello1,nomeModello1,descrizioneModello1,idCategoria1,nomeCategoria1,target1,idBrand1,nomeBrand1,descrizioneBrand1, statoPubblicazione1,prezzo1,tmp1,tmp2,tmp3));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdottiFull that = (ProdottiFull) o;
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
        output.put("id_modello",idModello);
        output.put("nome_modello",nomeModello);
        output.put("descrizione_modello",descrizioneModello);
        output.put("nome_categoria",nomeCategoria);
        output.put("id_categoria",idCategoria);
        output.put("target",target);
        output.put("id_brand",idBrand);
        output.put("nome_brand",nomeBrand);
        output.put("descrizione_brand",descrizioneBrand);
        output.put("stato_pubblicazione",statoPubblicazione);
        output.put("prezzo",prezzo);
        JSONArray array = new JSONArray();
        for (ProdottiTaglieEstratte t : taglieProdotto){
            array.put(new JSONObject(t.toString()));
        }
        output.put("taglieProdotto",array);
        output.put("url",url);
        output.put("nome_colore",nomeColore);
        return output.toString(4);
    }

    public static final class ProdottiTaglieEstratte implements Oggetti<ProdottiTaglieEstratte>{
        private Taglia taglia;
        private TaglieProdotti taglieProdotti;

        public ProdottiTaglieEstratte(Taglia taglia, TaglieProdotti taglieProdotti) {
            this.taglia = taglia;
            this.taglieProdotti = taglieProdotti;
        }

        @Override
        public Optional<ProdottiTaglieEstratte> convertDBToJava(ResultSet rs) {
            return Optional.empty();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj==null){ return false; }
            if (this==obj){ return  true; }
            if (!(obj instanceof ProdottiTaglieEstratte)){ return false; }
            ProdottiTaglieEstratte other = (ProdottiTaglieEstratte)(obj);
            return taglia.getTagliaEu().equals(other.taglia.getTagliaEu()) &&
                    taglia.getTagliaUk().equals(other.taglia.getTagliaUk()) &&
                    taglia.getTagliaUs().equals(other.taglia.getTagliaUs()) &&
                    taglieProdotti.getQuantita()==other.taglieProdotti.getQuantita();
        }

        @Override
        public String toString() {
            JSONObject output = new JSONObject();
            output.put("taglia",new JSONObject(taglia.toString()));
            output.put("taglia_prodotti",new JSONObject(taglieProdotti.toString()));
            return output.toString(4);
        }
    }
}
