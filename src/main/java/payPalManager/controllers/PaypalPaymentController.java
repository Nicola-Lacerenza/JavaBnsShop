package payPalManager.controllers;

import controllers.Controllers;
import payPalManager.models.PaypalPaymentsCreated;
import utility.Database;
import utility.QueryFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaypalPaymentController implements Controllers<PaypalPaymentsCreated>{
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
    public Optional<PaypalPaymentsCreated> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<PaypalPaymentsCreated> getAllObjects() {
        return new LinkedList<>();
    }

    @Override
    public List<PaypalPaymentsCreated> executeQuery(String query,Map<Integer,QueryFields<? extends Comparable<?>>> fields) {
        List<PaypalPaymentsCreated> output;
        try(Connection connection = Database.createConnection()){
            output = Database.executeGenericQuery(connection,query,fields,new PaypalPaymentsCreated());
        }catch (SQLException exception){
            exception.printStackTrace();
            output = new LinkedList<>();
        }
        return output;
    }
}