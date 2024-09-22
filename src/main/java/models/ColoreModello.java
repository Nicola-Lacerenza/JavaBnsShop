package models;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ColoreModello implements Oggetti<ColoreModello> {
  private final int id;
  private final int idColore;
  private final int idModello;

  public ColoreModello(int id,int idColore, int idModello) {
    this.id = id;
    this.idColore = idColore;
    this.idModello = idModello;
  }

  public ColoreModello(){
    this(0,0,0);
  }


  public int getId() {
    return id;
  }
  public int getIdColore() {
    return idColore;
  }

  public int getIdModello() {
    return idModello;
  }

  @Override
  public ColoreModello createObject() {
    return new ColoreModello();
  }

  @Override
  public Optional<ColoreModello> convertDBToJava(ResultSet rs) {
    try{
      int id1 = rs.getInt("id");
      int idColore = rs.getInt("id_colore");
      int idModello = rs.getInt("id_modello");
      return Optional.of(new ColoreModello(id1,idColore, idModello));
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
    output.put("id_modello",idModello);
    return output.toString(4);
  }
}
