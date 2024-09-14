package utility;

import models.Oggetti;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.stream.Collectors;

public class Database{
    //Variabili di servizio per interagire con il database
    private static final String DATABASE_NAME="mydb" ;
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="0000";
    private static final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/" + DATABASE_NAME;

    private Database(){}

    public static <T extends Oggetti<T>> boolean insertElement(Map<String,String> fields,String tableName){
        String nomi = fields.keySet().stream()
                .collect(Collectors.joining(","));
        String valori = fields.values().stream()
                .map(s -> "'"+s+"'")
                .collect(Collectors.joining(","));
        String query="INSERT INTO "+tableName+"("+nomi+") VALUES("+valori+")";
        boolean output;
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            statement.execute(query);
            output = true;
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
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

    public static boolean updateElement(int id,Map<String, String> fields, String tablename){
        StringBuilder query = new StringBuilder("UPDATE FROM "+tablename+" SET ");
        int actualField = 0;
        for(String field:fields.keySet()){
            query.append(field);
            query.append("='");
            query.append(fields.get(field));
            query.append("'");
            if(actualField < (fields.size()-1)){
                query.append(",");
            }
            actualField++;
        }
        query.append("WHERE id='");
        query.append(id);
        query.append("'");
        boolean output;
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            statement.execute(query.toString());
            output = true;
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
        }
        return output;
    }

    public static boolean deleteElement(int id, String tablename) {
        String query="DELETE FROM "+tablename+" WHERE id='"+id+"'";
        boolean output;
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            statement.execute(query);
            output = true;
        }catch(SQLException exception){
            output = false;
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
        }
        return output;
    }


    public static <E extends Oggetti<E>> Optional<E> getElement(int id, String tablename,E model) {
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
                E object = model.convertDBToJava(results);
                output = Optional.of(object);
            }
        }catch(SQLException exception){
            output = Optional.empty();
        }finally {
            if(results != null){
                try{
                    results.close();
                }catch(SQLException exception){
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
        }
        return output;
    }

    public static <E extends Oggetti<E>> List<E> getAllElements(String tablename,E model) {
        String query="SELECT * FROM " + tablename;
        List<E> output= new LinkedList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            results=statement.executeQuery(query);
            while (results.next()){
                E object = model.convertDBToJava(results);
                output.add(object);
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output.clear();
        }finally {
            if(results != null){
                try{
                    results.close();
                }catch(SQLException exception){
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    //Questo non é un errore è un warning
                    exception.printStackTrace();
                }
            }
        }
        return output;
    }
}
