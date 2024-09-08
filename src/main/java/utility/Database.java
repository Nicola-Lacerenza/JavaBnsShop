package utility;

import com.google.protobuf.ByteString;
import models.*;

import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class Database {

    // Metodi per interagire con il Database
    private static final String DATABASE_NAME="mydb" ;
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="0000";
    private static final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/" + DATABASE_NAME;


    private Database(){}

    public static <T extends Oggetti<T>> T insertElement(Map<String,String> fields,String tableName,T model){
        String nomi = fields.keySet().stream()
                .collect(Collectors.joining(","));
        String valori = fields.values().stream()
                .map(s -> "'"+s+"'")
                .collect(Collectors.joining(","));
        String query="INSERT INTO "+tableName+"("+nomi+") VALUES("+valori+")";
        System.out.println(query);
        T output;
        T object=model.createObject();
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            output=object.convertDBToJava(rs);
        }catch(SQLException exception){
            output = null;
            System.out.println("Errore1");
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //Questo non é un errore è un warning
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //Questo non é un errore è un warning
                    e.printStackTrace();
                }
            }
        }
        return output;
    }

    public static boolean updateElement(Map<String, String> fields, String tablename) {
        int idProdotto = Integer.parseInt(fields.get("idprodotto"));
        int idModello = Integer.parseInt(fields.get("idmodello"));
        int idTaglia = Integer.parseInt(fields.get("idtaglia"));
        double prezzo = Double.parseDouble(fields.get("prezzo"));
        int quantita = Integer.parseInt(fields.get("quantita"));
        int statopubblicazione = Integer.parseInt(fields.get("statopubblicazione"));
        String query="UPDATE FROM "+tablename+" SET idmodello='"+idModello+"', idtaglia='"+idTaglia+"',prezzo='"+prezzo+"',quantita='"+quantita+"',statopubblicazione='"+statopubblicazione+"' WHERE idprodotto='"+idProdotto+"'";
        boolean output=true;
        Connection connection = null;
        Statement statement = null;
        try{
            System.out.println("test0");
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            System.out.println("test1");
            statement=connection.createStatement();
            System.out.println("test2");
            statement.execute(query);
            System.out.println("test3");
            output=true;
        }catch(SQLException exception){
            output = false;
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    output = false;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    output= false;
                }
            }
        }
        return output;
    }

    public static boolean deleteElement(int id, String tablename) {
        String query="DELETE FROM "+tablename+" WHERE id='"+id+"'";
        boolean output=true;
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            statement.execute(query);
            output=true;
        }catch(SQLException exception){
            output = false;
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    output = false;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    output= false;
                }
            }
        }
        return output;
    }


    public static <E extends Oggetti<E>> Optional<E> getElement(int id, String tablename) {
        String query="SELECT * FROM "+tablename+" WHERE id='"+id+"'";
        Optional<E> output=Optional.empty();
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            results=statement.executeQuery(query);
            while (results.next()){
                E object= null;

                if (tablename.contains("brand")){
                    String nome = results.getString("nome");
                    String descrizione = results.getString("descrizione");
                    object = (E) new Brand(id,nome,descrizione);
                }

                if (tablename.contains("categoria")){
                    String nomecategoria = results.getString("nomecategoria");
                    object = (E) new Categoria(id,nomecategoria);
                }

                if (tablename.contains("colore")){
                    String nome = results.getString("nome");
                    String rgb = results.getString("rgb");
                    String hex = results.getString("hex");
                    object = (E) new Colore(id,nome,rgb,hex);
                }

                if (tablename.contains("colore_has_modello")){
                    int idcolore = results.getInt("idcolore");
                    int idmodello = results.getInt("idmodello");
                    object = (E) new ColoreModello(idcolore,idmodello);
                }

                if (tablename.contains("fornitori")){
                    String nome = results.getString("nome");
                    String cognome = results.getString("cognome");
                    object = (E) new Fornitori(id,nome,cognome);
                }

                if (tablename.contains("fornitori_has_prodotti")){
                    int idprodotti = results.getInt("idprodotti");
                    int idfornitore = results.getInt("idfornitore");
                    Data data = (Data) results.getDate("data");
                    int importo = results.getInt("importo");
                    String descrizione = results.getString("descrizione");
                    object = (E) new FornitoriProdotti(idprodotti,idfornitore, (Calendar) data,importo,descrizione);
                }

                if (tablename.contains("immagini")){
                    int idprodotti = results.getInt("idprodotti");
                    String url = results.getString("url");
                    object = (E) new Immagini(id,idprodotti,url);
                }

                if (tablename.contains("modello")){
                    int idcategoria = results.getInt("idcategoria");
                    int idbrand = results.getInt("idbrand");
                    String nome = results.getString("nome");
                    String descrizione = results.getString("descrizione");
                    object = (E) new Modello(id,idcategoria,idbrand,nome,descrizione);
                }

                if (tablename.contains("prodotti")){
                    int idModello = results.getInt("idmodello");
                    int idTaglia = results.getInt("idtaglia");
                    double prezzo = results.getDouble("prezzo");
                    int quantita = results.getInt("quantita");
                    int statopubblicazione = results.getInt("statopubblicazione");
                    object = (E) new Prodotti(id,idModello,idTaglia,prezzo,quantita,statopubblicazione);
                }

                if (tablename.contains("taglia")){
                    String taglia = results.getString("taglia");
                    object = (E) new Taglia(id,taglia);
                }
                output=Optional.of(object);
            }
        }catch(SQLException exception){
            output=Optional.empty();
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    output=Optional.empty();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    output=Optional.empty();
                }
            }
        }
        return output;
    }

    public static <E extends Oggetti<E>> List<E> getAllElements(String tablename) {
        String query="SELECT * FROM " + tablename;
        List<E> output= new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            results=statement.executeQuery(query);
            while (results.next()){
                int idProdotto = results.getInt("idprodotto");
                int idModello = results.getInt("idmodello");
                int idTaglia = results.getInt("idtaglia");
                double prezzo = results.getDouble("prezzo");
                int quantita = results.getInt("quantita");
                int statopubblicazione = results.getInt("statopubblicazione");
                Prodotti prodotto = new Prodotti(idProdotto,idModello,idTaglia,prezzo,quantita,statopubblicazione);
                output.add((E)prodotto);
            }
        }catch(SQLException exception){
            output.clear();
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    output.clear();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    output.clear();
                }
            }
        }
        return output;
    }
}
