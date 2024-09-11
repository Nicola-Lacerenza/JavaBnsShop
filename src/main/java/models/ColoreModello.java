package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class ColoreModello implements Oggetti<ColoreModello> {

  private int idColore;

  private int idModello;

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
  public ColoreModello convertDBToJava(ResultSet rs) {
    try{
      int idColore = rs.getInt("idColore");
      int idModello = rs.getInt("idModello");
      return new ColoreModello(idColore, idModello);
    }catch (SQLException e){
      e.printStackTrace();
      return null;
    }
  }
}
