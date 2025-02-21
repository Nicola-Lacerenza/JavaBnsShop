package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ColoreProdotti implements Oggetti<ColoreProdotti> {
  private final int id;
  private final int idColore;
  private final int idProdotto;

  public ColoreProdotti(int id, int idColore, int idProdotto) {
    this.id = id;
    this.idColore = idColore;
    this.idProdotto = idProdotto;
  }

  public ColoreProdotti(){
    this(0,0,0);
  }


  public int getId() {
    return id;
  }
  public int getIdColore() {
    return idColore;
  }

  public int getIdProdotto() {
    return idProdotto;
  }

  @Override
  public ColoreProdotti createObject() {
    return new ColoreProdotti();
  }

  @Override
  public Optional<ColoreProdotti> convertDBToJava(ResultSet rs) {
    try{
      int id1 = rs.getInt("id");
      int idColore1 = rs.getInt("id_colore");
      int idProdotto1 = rs.getInt("id_prodotto");
      return Optional.of(new ColoreProdotti(id1,idColore1, idProdotto1));
    }catch (SQLException e){
      e.printStackTrace();
      return Optional.empty();
    }
  }
  @Override
  public String toString() {
    JSONObject output = new JSONObject();
    output.put("id",id);
    output.put("id_colore",idColore);
    output.put("id_prodotto",idProdotto);
    return output.toString(4);
  }
}
