package payPalManager;

import bnsshop.bnsshop.RegisterServlet;
import controllers.Controllers;
import payPalManager.PaypalToken;
import utility.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayPalTokenController implements Controllers<PaypalToken> {

    public PayPalTokenController(){
    }
    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return Database.insertElement(request,"paypal_token");
    }

    @Override
    public boolean updateObject(int id, Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<PaypalToken> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<PaypalToken> getAllObjects() {
        List<PaypalToken> list =  Database.getAllElements("paypal_token", new PaypalToken());
        Collections.sort(list);
        return list;
    }

    @Override
    public List<PaypalToken> executeQuery(String query) {
        return null;
    }


}
