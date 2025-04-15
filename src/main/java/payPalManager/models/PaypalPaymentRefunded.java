package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PaypalPaymentRefunded implements Cloneable, Serializable,PaypalModels<PaypalPaymentRefunded> {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaypalPaymentRefunded() {}

    private PaypalPaymentRefunded(PaypalPaymentRefunded original) {
        this();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new PaypalPaymentRefunded(this);
    }

    @Override
    public PaypalPaymentRefunded parseObjectFromJSON(JSONObject json) throws ParserException {
        return new PaypalPaymentRefunded();
    }

    @Override
    public PaypalPaymentRefunded copy() {
        return new PaypalPaymentRefunded(this);
    }

    @Override
    public JSONObject printJSONObject() {
        return new JSONObject();
    }

    @Override
    public Optional<PaypalPaymentRefunded> convertDBToJava(ResultSet rs) throws SQLException {
        return Optional.empty();
    }
}