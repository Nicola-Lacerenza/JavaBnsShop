package utility;

import java.util.Map;
import java.sql.*;

public class Database {

    private static final String DATABASE_NAME="dbbnsshop";
    private static final String DATABASE_USERNAME="root";
    private static final String DATABASE_PASSWORD="";
    private static final String DATABASE_URL="jdbc:mysql://localhost:3306/" + DATABASE_NAME;


    private Database(){}

    public static boolean insertElement(Map<String,String> fields,String tableName ){
        String query="";
        Connection connection;
        Statement statement;
        try{
            connection=DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            statement=connection.createStatement();
        }

    }
}
