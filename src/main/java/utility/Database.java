package utility;

import models.Oggetti;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Database{
    private static final String DATABASE_NAME = "mydb" ;
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "root";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    private Database(){}

    public static Connection createConnection() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException exception){
            throw new SQLException(exception.getMessage(),exception);
        }
        return DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
    }

    public static boolean executeTransaction(TransactionOperation operations){
        boolean success = false;
        try(Connection connection = createConnection()){
            connection.setAutoCommit(false);
            success = operations.execute(connection);
            if(success){
                System.out.println("Queries executed correctly in the database.");
                connection.commit();
            }else{
                System.err.println("Error executing the queries in the database.");
                connection.rollback();
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            success = false;
        }
        return success;
    }

    public static int insertElement(Connection connection,String tableName,Map<Integer,QueryFields<? extends Comparable<?>>> fields){
        StringBuilder nomi = new StringBuilder();
        StringBuilder valori = new StringBuilder();
        for(int i = 0;i < fields.size();i++){
            QueryFields<? extends Comparable<?>> attuale = fields.get(i);
            nomi.append(attuale.getFieldName());
            valori.append("?");
            if(i < (fields.size() - 1)){
                nomi.append(",");
                valori.append(",");
            }
        }
        String query = "INSERT INTO " + tableName + "(" + nomi + ") VALUES (" + valori + ")";
        int output;
        try(PreparedStatement statement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
            loadQueryParameters(statement,fields);
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    output = generatedKeys.getInt(1);
                }else{
                    throw new SQLException("Error extracting the generated key of the inserted row.");
                }
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output = -1;
        }
        return output;
    }

    public static boolean updateElement(Connection connection,String tableName,int id,Map<Integer,QueryFields<? extends Comparable<?>>> fields){
        StringBuilder campi = new StringBuilder();
        for(int i = 0;i < fields.size();i++){
            QueryFields<? extends Comparable<?>> attuale = fields.get(i);
            campi.append(attuale.getFieldName());
            campi.append(" = ");
            campi.append("?");
            if(i < (fields.size() - 1)){
                campi.append(",");
            }
        }
        String query = "UPDATE " + tableName + " SET " + campi + " WHERE id = ?";
        boolean output;
        try(PreparedStatement statement = connection.prepareStatement(query)){
            loadQueryParameters(statement,fields);
            statement.setInt(fields.size() + 1,id);
            statement.executeUpdate();
            output = true;
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    public static boolean deleteElement(Connection connection,String tableName,int id){
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        boolean output;
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,id);
            statement.executeUpdate();
            output = true;
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    public static <T extends Oggetti<T>> Optional<T> getElement(Connection connection,String tableName,int id,T templateModel){
        List<T> output= new LinkedList<>();
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Optional<T> object = templateModel.convertDBToJava(result);
                    object.ifPresent(output::add);
                }
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output.clear();
        }
        if(output.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(output.getFirst());
    }

    public static <T extends Oggetti<T>> List<T> getAllElements(Connection connection,String tableName,T templateModel){
        List<T> output= new LinkedList<>();
        String query = "SELECT * FROM " + tableName;
        try(PreparedStatement statement = connection.prepareStatement(query)){
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Optional<T> object = templateModel.convertDBToJava(result);
                    object.ifPresent(output::add);
                }
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output.clear();
        }
        return output;
    }

    public static <T extends Oggetti<T>> List<T> executeGenericQuery(Connection connection,String query,Map<Integer,QueryFields<? extends Comparable<?>>> fields,T templateModel){
        List<T> output= new LinkedList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            loadQueryParameters(statement,fields);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Optional<T> object = templateModel.convertDBToJava(result);
                    object.ifPresent(output::add);
                }
            }
        }catch(SQLException exception){
            exception.printStackTrace();
            output.clear();
        }
        return output;
    }

    public static boolean executeGenericUpdate(Connection connection,String query,Map<Integer,QueryFields<? extends Comparable<?>>> fields){
        boolean output;
        try(PreparedStatement statement = connection.prepareStatement(query)){
            loadQueryParameters(statement,fields);
            statement.executeUpdate();
            output = true;
        }catch(SQLException exception){
            exception.printStackTrace();
            output = false;
        }
        return output;
    }

    private static void loadQueryParameters(PreparedStatement statement,Map<Integer,QueryFields<? extends Comparable<?>>> fields) throws SQLException{
        for(int index = 0;index < fields.size();index++){
            QueryFields<? extends Comparable<?>> attuale = fields.get(index);
            if(attuale.getFieldType().equals(TipoVariabile.string)){
                String stringa = (String)(attuale.getFieldValue());
                statement.setString(index + 1,stringa);
            }else if(attuale.getFieldType().equals(TipoVariabile.longNumber)){
                Integer numero = (Integer)(attuale.getFieldValue());
                statement.setInt(index + 1,numero);
            }else if(attuale.getFieldType().equals(TipoVariabile.realNumber)){
                Double numero = (Double)(attuale.getFieldValue());
                statement.setDouble(index + 1,numero);
            }else{

                //date non implementate (type=date)
                throw new SQLException("Date type doesn't available.");

            }
        }
    }

    @FunctionalInterface
    public interface TransactionOperation{
        boolean execute(Connection connection);
    }
}