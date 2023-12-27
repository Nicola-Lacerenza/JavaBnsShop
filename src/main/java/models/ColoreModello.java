package models;

public class ColoreModello implements Oggetti<ColoreModello> {

  private int idColore;

  private int idModello;

  public ColoreModello(int idColore, int idModello) {
    this.idColore = idColore;
    this.idModello = idModello;
  }

  public int getIdColore() {
    return idColore;
  }

  public int getIdModello() {
    return idModello;
  }
}
