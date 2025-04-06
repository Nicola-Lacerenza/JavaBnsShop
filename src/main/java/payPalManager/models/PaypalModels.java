package payPalManager.models;

import exceptions.ParserException;
import models.Oggetti;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface PaypalModels<T extends PaypalModels<T>> extends Oggetti<T> {
    T parseObjectFromJSON(JSONObject json) throws ParserException;

    T copy();

    JSONObject printJSONObject();

}
