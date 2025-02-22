package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ImmaginiProdotti implements Oggetti<ImmaginiProdotti> {
  private final int id;
  private final int idImmagine;
  private final int idProdotto;

  public ImmaginiProdotti(int id, int idImmagine, int idProdotto) {
    this.id = id;
    this.idImmagine = idImmagine;
    this.idProdotto = idProdotto;
  }

  public ImmaginiProdotti(){
    this(0,0,0);
  }


  public int getId() {
    return id;
  }
  public int getidImmagine() {
    return idImmagine;
  }

  public int getidProdotto() {
    return idProdotto;
  }

  @Override
  public Optional<ImmaginiProdotti> convertDBToJava(ResultSet rs) {
    try{
      int id1 = rs.getInt("id");
      int idImmagine = rs.getInt("id_Immagine");
      int idProdotto = rs.getInt("id_Prodotto");
      return Optional.of(new ImmaginiProdotti(id1,idImmagine, idProdotto));
    }catch (SQLException e){
      e.printStackTrace();
      return Optional.empty();
    }
  }
  @Override
  public String toString() {
    JSONObject output = new JSONObject();
    output.put("id",id);
    output.put("id_Immagine",idImmagine);
    output.put("id_Prodotto",idProdotto);
    return output.toString(4);
  }
}
