package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ColoreModello implements Oggetti<ColoreModello> {
  private final int idColore;
  private final int idModello;

  public ColoreModello(int idColore, int idModello) {
    this.idColore = idColore;
    this.idModello = idModello;
  }

  public ColoreModello(){
    this(0,0);
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
      int idColore = rs.getInt("id_colore");
      int idModello = rs.getInt("id_modello");
      return Optional.of(new ColoreModello(idColore, idModello));
    }catch (SQLException e){
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
