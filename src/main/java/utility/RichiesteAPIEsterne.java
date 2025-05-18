package utility;

import models.Oggetti;
import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public abstract class RichiesteAPIEsterne<T extends Oggetti<T>> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String apiBaseURL;
    private final String accessToken;

    public RichiesteAPIEsterne(String apiBaseURL,String accessToken){
        this.apiBaseURL = apiBaseURL;
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public abstract String getEndpoint();

    public abstract String getMethod();

    public abstract Map<String,String> getHeaders();

    public abstract String getBody();

    public abstract Optional<T> parseResponse(JSONObject jsonResponse);

    public Optional<T> execute(){
        //make the https request and read the response in json format.
        String address = apiBaseURL + getEndpoint();
        String method = getMethod();
        Map<String,String> headers = getHeaders();
        String body = getBody();
        Optional<JSONObject> response = RichiestaHttps.requestAPIConnection(address,method,headers,body);
        if(response.isEmpty()){
            return Optional.empty();
        }
        JSONObject jsonResponse = response.get();
        return parseResponse(jsonResponse);
    }
}
