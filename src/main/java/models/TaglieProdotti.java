package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TaglieProdotti implements Oggetti<TaglieProdotti> {
  private final int id;
  private final int idTaglia;
  private final int idProdotto;
  private final int quantita;

  public TaglieProdotti(int id, int idTaglia, int idProdotto,int quantita) {
    this.id = id;
    this.idTaglia = idTaglia;
    this.idProdotto = idProdotto;
    this.quantita = quantita;
  }

  public TaglieProdotti(){
    this(0,0,0,0);
  }


  public int getId() {
    return id;
  }
  public int getidTaglia() {
    return idTaglia;
  }

  public int getidProdotto() {
    return idProdotto;
  }
  public int getQuantita() { return quantita; }

  @Override
  public TaglieProdotti createObject() {
    return new TaglieProdotti();
  }

  @Override
  public Optional<TaglieProdotti> convertDBToJava(ResultSet rs) {
    try{
      int id1 = rs.getInt("id");
      int idTaglia = rs.getInt("id_Taglia");
      int idProdotto = rs.getInt("id_Prodotto");
      int quantita = rs.getInt("quantita");
      return Optional.of(new TaglieProdotti(id1,idTaglia, idProdotto,quantita));
    }catch (SQLException e){
      e.printStackTrace();
      return Optional.empty();
    }
  }
  @Override
  public String toString() {
    JSONObject output = new JSONObject();
    output.put("id",id);
    output.put("id_Taglia",idTaglia);
    output.put("id_Prodotto",idProdotto);
    output.put("quantita",quantita);
    return output.toString(4);
  }
}
