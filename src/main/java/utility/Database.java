package utility;

import bnsshop.bnsshop.RegisterServlet;
import models.Oggetti;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Database{
    //Variabili di servizio per interagire con il database
    private static final String DATABASE_NAME="mydb" ;
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="";
    private static final String DATABASE_URL="jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    private Database(){}

    public static <T extends Oggetti<T>> boolean insertElement(Map<Integer, RegisterServlet.RegisterFields> fields, String tableName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String nomi = "";
        String valori = "";
        for (int i = 0;i<fields.size();i++){
            RegisterServlet.RegisterFields attuale = fields.get(i);
            nomi+=attuale.getKey();
            valori+="'" + attuale.getValue()+"'";
            if (i<(fields.size()-1)){
                nomi+=",";
                valori+=",";
            }
        }
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

    public static boolean updateElement(int id,Map<Integer, RegisterServlet.RegisterFields> fields, String tablename){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder query = new StringBuilder("UPDATE "+tablename+" SET ");
        int actualField = 0;
        for(int i=0;i<fields.size();i++){
            RegisterServlet.RegisterFields field=fields.get(i);
            query.append(field.getKey());
            query.append("='");
            query.append(field.getValue());
            query.append("'");
            if(actualField < (fields.size()-1)){
                query.append(",");
            }
            actualField++;
        }
        query.append(" WHERE id='");
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        List<E> list = executeGenericQuery(tablename,model,query);
        if (list.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(list.getFirst());
    }

    public static <E extends Oggetti<E>> List<E> getAllElements(String tablename,E model) {
        String query="SELECT * FROM " + tablename;
        return executeGenericQuery(tablename,model,query);
    }

    public static <E extends Oggetti<E>> List<E> executeGenericQuery(String tablename,E model,String query) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<E> output= new LinkedList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            results=statement.executeQuery(query);
            while (results.next()){
                Optional<E> object = model.convertDBToJava(results);
                object.ifPresent(output::add);
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

    public static boolean executeQueries(List<String> queries) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        boolean output;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            connection.setAutoCommit(false);
            List<PreparedStatement> statementList = new LinkedList<>();
            for (String query:queries){
                statementList.add(connection.prepareStatement(query));
                connection.commit();
            }
            output=true;
        }catch (SQLException e){
            e.printStackTrace();
            if (connection != null){
                try {
                    connection.rollback();
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
            output=false;
        }finally {
            if (connection!= null){
                try {
                    connection.close();
                } catch (SQLException e2) {
                    //Connessione non chiusa questo è un warning da gestire
                    e2.printStackTrace();
                }
            }
        }
        return output;
    }
}
