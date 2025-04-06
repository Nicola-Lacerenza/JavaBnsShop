package payPalManager.controllers;
import bnsshop.bnsshop.RegisterServlet;
import com.antoniotassone.paypal.models.PaypalTokens;
import com.antoniotassone.paypal.models.PaypalTokensTableSchema;
import controllers.Controllers;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PaypalTokensController implements Controllers<PaypalTokens> {
    //private final TableSchema<PaypalTokensTableSchema,PaypalTokens> model;

    public PaypalTokensController(){
        //model = new PaypalTokensTableSchema();
    }


    @Override
    public boolean insertObject(Map<Integer, RegisterServlet.RegisterFields> request) {
        return false;
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
    public Optional<PaypalTokens> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<PaypalTokens> getAllObjects() {
        return null;
    }

    @Override
    public List<PaypalTokens> executeQuery(String query) {
        return null;
    }
}