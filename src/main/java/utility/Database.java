package utility;

import com.google.protobuf.ByteString;

import java.util.Map;
import java.sql.*;

public class Database {

    private static final String DATABASE_NAME="mydb";
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="0000";
    private static final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/" + DATABASE_NAME;


    private Database(){}

    public static boolean insertElement(Map<String,String> fields,String tableName ){
        String query="INSERT INTO PRODOTTI(id_modello,id_taglia,prezzo,quantita,statopubblicazione) VALUES('1','1','1','1','1')";
        boolean output=true;
        Connection connection = null;
        Statement statement = null;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
            if (statement.execute(query)){
                System.out.println("Inserimento effettuato");
            }else{
                System.out.println("Errore");
            }
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
}
