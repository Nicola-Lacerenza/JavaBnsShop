package utility;

import com.google.gson.JsonArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GestioneFileTesto {

    private GestioneFileTesto(){}

    public static Map<String,String> leggiFile(String nomeFile){
        Map<String,String> output = new HashMap<>();
        FileInputStream fis;
        try{
            String directory = System.getProperty("user.home");
            fis = new FileInputStream(directory+ "\\" +nomeFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
        InputStreamReader isr = new InputStreamReader(fis,StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(isr);
        try {
            String line = in.readLine();
            while(line!=null){
                String[] detail = line.split(":");
                output.put(detail[0],detail[1]);
                line= in.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
            output.clear();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    public static JSONObject leggiFileJSON(String nomeFile){
        JSONObject output;
        FileInputStream fis;
        try{
            String directory = System.getProperty("user.home");
            fis = new FileInputStream(directory+ "\\" +nomeFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return new JSONObject();
        }
        InputStreamReader isr = new InputStreamReader(fis,StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(isr);
        List<String> lines = new LinkedList<>();
        try {
            String line = in.readLine();
            while(line!=null){
                lines.add(line);
                line= in.readLine();
            }
            StringBuilder json = new StringBuilder();
            for (String l:lines){
                json.append(l);
            }
            String object = json.toString();
            output = new JSONObject(object);
        }catch (IOException e){
            e.printStackTrace();
            output = new JSONObject();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }
}
