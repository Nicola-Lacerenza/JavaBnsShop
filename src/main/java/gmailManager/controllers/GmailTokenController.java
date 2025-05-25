package gmailManager.controllers;

import controllers.Controllers;
import gmailManager.models.GmailToken;
import utility.Database;
import utility.QueryFields;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GmailTokenController implements Controllers<GmailToken> {
    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return 0;
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
    public Optional<GmailToken> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<GmailToken> getAllObjects() {
        List<GmailToken> tokens;
        try(Connection connection = Database.createConnection()){
            tokens = Database.getAllElements(connection,"gmail_token",new GmailToken());

        }catch (SQLException e){
            e.printStackTrace();
            tokens = new LinkedList<>();
        }
        return tokens;
    }

    @Override
    public List<GmailToken> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}
