package payPalManager;

import org.json.JSONObject;

import java.util.Calendar;

public class LinkCreato {
    private final String method;
    private final String rel;
    private final String href;

    public LinkCreato(String method,String rel,String href){
        this.method= method;
        this.rel= rel;
        this.href= href;
    }

    public LinkCreato(){
        this("","","");
    }

    public String getMethod() {
        return method;
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        JSONObject output = new JSONObject();
        output.put("method",method);
        output.put("rel",rel);
        output.put("href",href);
        return output.toString(4);
    }
}
