package utility;

import com.google.protobuf.ByteString;
import models.Oggetti;
import models.Prodotti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.util.Optional;

public class Database {

    // Metodi per interagire con il Database
    private static final String DATABASE_NAME="mydb" ;
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="0000";
    private static final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/" + DATABASE_NAME;


    private Database(){}

    public static boolean insertElement(Map<String,String> fields,String tableName ){
        List<String> nomecampi = new ArrayList<>();
        List<String> valoreCampi = new ArrayList<>();
        if (tableName.contains("prodotti")){
            nomecampi.add("idmodello");
            nomecampi.add("idtaglia");
            nomecampi.add("prezzo");
            nomecampi.add("quantita");
            nomecampi.add("statopubblicazione");
            valoreCampi.add(fields.get("idmodello"));
            valoreCampi.add(fields.get("idtaglia"));
            valoreCampi.add(fields.get("prezzo"));
            valoreCampi.add(fields.get("quantita"));
            valoreCampi.add(fields.get("statopubblicazione"));

        }
        String nomi = nomecampi.stream()
                .map((element) =>)
        String query="INSERT INTO "+tableName+"(idprodotto,idmodello,idtaglia,prezzo,quantita,statopubblicazione) VALUES('"+idProdotto+"','"+idModello+"','"+idTaglia+"','"+prezzo+"','"+quantita+"','"+statopubblicazione+"')";
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

    public static boolean deleteElement(int id, String tablename) {
        String query="DELETE FROM "+tablename+" WHERE idprodotto='"+id+"'";
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
        String query="SELECT * FROM "+tablename+" WHERE idprodotto='"+id+"'";
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
                //Table Prodotti
                if (tablename.contains("prodotti")){
                    int idProdotto = results.getInt("idprodotto");
                    int idModello = results.getInt("idmodello");
                    int idTaglia = results.getInt("idtaglia");
                    double prezzo = results.getDouble("prezzo");
                    int quantita = results.getInt("quantita");
                    int statopubblicazione = results.getInt("statopubblicazione");
                    object = (E) new Prodotti(idProdotto,idModello,idTaglia,prezzo,quantita,statopubblicazione);
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
