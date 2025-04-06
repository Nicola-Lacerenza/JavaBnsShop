package utility;

import bnsshop.bnsshop.RegisterServlet;
import models.Oggetti;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Database{
    //Variabili di servizio per interagire con il database
    private static final String DATABASE_NAME="mydb" ;
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="root";
    private static final String DATABASE_URL="jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    private Database(){}

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getDatabaseUsername() {
        return DATABASE_USERNAME;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }

    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }

    /*public static <T extends Oggetti<T>> boolean insertElement1(Map<Integer, RegisterServlet.RegisterFields> fields, String tableName) {
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
            valori+= "?";
            if (i<(fields.size()-1)){
                nomi+=",";
                valori+=",";
            }
        }

        String query="INSERT INTO "+tableName+"("+nomi+") VALUES("+valori+")";
        boolean output;
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.prepareStatement(query);

            for (int i = 0;i<fields.size();i++){
                RegisterServlet.RegisterFields attuale = fields.get(i);
                statement.setString(i+1,attuale.getValue());

            }
            statement.executeUpdate();
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

    }*/

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

    public static <T extends Oggetti<T>> int insertElementExtractId(Map<Integer, QueryFields> fields, String tableName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String nomi = "";
        String valori = "";
        for (int i = 0;i<fields.size();i++){
            QueryFields attuale = fields.get(i);
            nomi+=attuale.getFieldName();
            valori += "?";
            if (i<(fields.size()-1)){
                nomi+=",";
                valori+=",";
            }
        }
        String query="INSERT INTO "+tableName+"("+nomi+") VALUES("+valori+")";
        int output;
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int index : fields.keySet()){
                if (fields.get(index).getFieldType().equals(TipoVariabile.string)){
                    statement.setString(index,(String)fields.get(index).getFieldValue());
                } else if (fields.get(index).getFieldType().equals(TipoVariabile.longNumber)){
                    statement.setInt(index,(Integer) fields.get(index).getFieldValue());
                }
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                output = rs.getInt("id");
            } else {
                throw new SQLException("Id non estratto");
            }
            statement.executeUpdate();

        }catch(SQLException exception){
            exception.printStackTrace();
            output = -1;
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
            throw new RuntimeException("Driver JDBC non trovato", e);
        }

        Connection connection = null;
        List<PreparedStatement> statementList = new LinkedList<>();

        boolean output = false;
        try {
            // Apertura connessione
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            connection.setAutoCommit(false); // Avvia transazione

            // Prepara ed esegue le query
            for (String query : queries) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                statementList.add(preparedStatement);
                preparedStatement.executeUpdate(); // Esegue la query
            }

            connection.commit(); // Commit delle modifiche solo se tutte le query hanno successo
            output = true;
        } catch (SQLException e) {
            // Gestione errori e rollback in caso di fallimento
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            output = false;
        } finally {
            // Chiusura connessione e statement
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        // Chiude tutti i PreparedStatement
        for (PreparedStatement stmt : statementList) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        return output;
    }
}
