package payPalManager.controllers;

import controllers.Controllers;
import payPalManager.models.PaypalTokens;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PaypalTokensController implements Controllers<PaypalTokens> {
    public PaypalTokensController(){}

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return -1;
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
    public Optional<PaypalTokens> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<PaypalTokens> getAllObjects() {
        List<PaypalTokens> output;
        try(Connection connection = Database.createConnection()){
            output = Database.getAllElements(connection,"paypal_token",new PaypalTokens());
        }catch (SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }

    @Override
    public List<PaypalTokens> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return new LinkedList<>();
    }
}