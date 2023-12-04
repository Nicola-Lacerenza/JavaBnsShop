package utility;

import com.google.protobuf.ByteString;

import java.util.Map;
import java.sql.*;

public class Database {

    // Metodi per interagire con il Database
    private static final String DATABASE_NAME="mydb";
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="0000";
    private static final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/" + DATABASE_NAME;


    private Database(){}

    public static boolean insertElement(Map<String,String> fields,String tableName ){
        int idModello = Integer.parseInt(fields.get("id_modello"));
        int idTaglia = Integer.parseInt(fields.get("id_taglia"));
        double prezzo = Double.parseDouble(fields.get("prezzo"));
        int quantita = Integer.parseInt(fields.get("quantita"));
        int statopubblicazione = Integer.parseInt(fields.get("statopubblicazione"));
        String query="INSERT INTO "+tableName+"(id_modello,id_taglia,prezzo,quantita,statopubblicazione) VALUES('"+idModello+"','"+idTaglia+"','"+prezzo+"','"+quantita+"','"+statopubblicazione+"')";
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
}
