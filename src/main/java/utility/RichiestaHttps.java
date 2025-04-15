package utility;

import org.json.JSONException;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

public class RichiestaHttps {
    private RichiestaHttps(){}

    public static Optional<JSONObject> requestAPIConnection(String address, String method, Map<String,String> headers, String body){
        URL url;
        try{
            url = URI.create(address).toURL();
        }catch(MalformedURLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        HttpsURLConnection connection;
        try{
            connection = (HttpsURLConnection)(url.openConnection());
        }catch(IOException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        try{
            connection.setRequestMethod(method);
        }catch(ProtocolException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        connection.setDoOutput(true);
        for(String header:headers.keySet()){
            connection.setRequestProperty(header,headers.get(header));
        }
        StringBuilder response;
        int responseCode;
        try{
            OutputStream out = connection.getOutputStream();
            out.write(body.getBytes());
            out.flush();
            out.close();
            responseCode = connection.getResponseCode();
            InputStream stream;
            if(responseCode >= 400){
                stream = connection.getErrorStream();
            }else{
                stream = connection.getInputStream();
            }
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader in = new BufferedReader(isr);
            response = new StringBuilder();
            String line = in.readLine();
            while(line != null){
                response.append(line);
                line = in.readLine();
            }
            in.close();
            isr.close();
            stream.close();
        }catch(IOException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        String bodyResponse = response.toString();
        if(responseCode >= 400){
            System.err.println(bodyResponse);
            return Optional.empty();
        }
        JSONObject output;
        try{
            output = new JSONObject(bodyResponse);
        }catch(JSONException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(output);
    }
}
