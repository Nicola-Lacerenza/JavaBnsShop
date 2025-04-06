package payPalManager.models;

import exceptions.ParserException;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class ExperienceContext implements Cloneable,Serializable,Comparable<ExperienceContext>,PaypalModels<ExperienceContext>{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String paymentMethodPreference;
    private final String shippingPreference;
    private final String locale;
    private final String returnUrl;
    private final String cancelUrl;
    private final String brandName;
    private final String landingPage;
    private final String userAction;

    public ExperienceContext(String locale,String returnUrl,String cancelUrl){
        this.paymentMethodPreference = "IMMEDIATE_PAYMENT_REQUIRED";
        this.shippingPreference = "GET_FROM_FILE";
        this.locale = locale;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
        this.brandName = "Antonio Tassone";
        this.landingPage = "LOGIN";
        this.userAction = "PAY_NOW";
    }

    private ExperienceContext(ExperienceContext original){
        this.paymentMethodPreference = original.paymentMethodPreference;
        this.shippingPreference = original.shippingPreference;
        this.locale = original.locale;
        this.returnUrl = original.returnUrl;
        this.cancelUrl = original.cancelUrl;
        this.brandName = original.brandName;
        this.landingPage = original.landingPage;
        this.userAction = original.userAction;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new ExperienceContext(this);
    }

    @Override
    public String toString(){
        return printJSONObject().toString(4);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof ExperienceContext)){
            return false;
        }
        ExperienceContext other = (ExperienceContext)(obj);
        return locale.equals(other.locale) && returnUrl.equals(other.returnUrl) && cancelUrl.equals(other.cancelUrl);
    }

    @Override
    public int hashCode(){
        long hash = locale.hashCode() + returnUrl.hashCode() + cancelUrl.hashCode();
        if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }else if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(ExperienceContext o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        int compare1 = locale.compareTo(o.locale);
        if(compare1 == 0){
            int compare2 = returnUrl.compareTo(o.returnUrl);
            if(compare2 == 0){
                return cancelUrl.compareTo(o.cancelUrl);
            }
            return compare2;
        }
        return compare1;
    }

    @Override
    public ExperienceContext copy(){
        return new ExperienceContext(this);
    }

    @Override
    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("payment_method_preference",paymentMethodPreference);
        output.put("shipping_preference",shippingPreference);
        output.put("locale",locale);
        output.put("return_url",returnUrl);
        output.put("cancel_url",cancelUrl);
        output.put("brand_name",brandName);
        output.put("landing_page",landingPage);
        output.put("user_action",userAction);
        return output;
    }

    @Override
    public Optional<ExperienceContext> convertDBToJava(ResultSet row) throws SQLException{
        throw new SQLException("For this class the method extractFromDatabase isn't implemented.");
    }

    @Override
    public ExperienceContext parseObjectFromJSON(JSONObject json) throws ParserException {
        throw new ParserException("For this class the method parseObjectFromJSON isn't implemented.");
    }
}