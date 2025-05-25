package gmailManager.controllers;

import controllers.Controllers;
import gmailManager.models.EmailMessage;
import utility.Database;
import utility.QueryFields;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmailMessageController implements Controllers<EmailMessage> {
    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        int id;
        try(Connection connection= Database.createConnection()) {
            id = Database.insertElement(connection,"email_inviate",request);
        }catch (SQLException e){
            e.printStackTrace();
            id=-1;
        }
        return id;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<EmailMessage> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<EmailMessage> getAllObjects() {
        return null;
    }

    @Override
    public List<EmailMessage> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}
