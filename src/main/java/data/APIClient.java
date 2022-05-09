package data;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.*;

public class APIClient {
    public static OkHttpClient client = new OkHttpClient();
    public static Gson gson = new Gson();
    
    public static Request request(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }
    
    public static Request authedRequest(String url, String headerKey, String token){
        Request request = new Request.Builder()
                .url(url)
                .header(headerKey, token)
                .build();
        return request;
    }
    public static Request postRequest(String url, RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }
    public static Request authedPostRequest(String url, RequestBody body, String headerKey, String token){
        Request request = new Request.Builder()
                .url(url)
                .header(headerKey, token)
                .post(body)
                .build();
        return request;
    }
    public static Request authedPutRequest(String url, RequestBody body, String headerKey, String token){
        Request request = new Request.Builder()
                .url(url)
                .header(headerKey, token)
                .put(body)
                .build();
        return request;
    }
    
    public static Response fire(Request request) throws IOException{
        Response response = client.newCall(request).execute();
        //System.out.println(response.toString());
        return response;
    }
    
//    public static Map<String, Object> jsonStringToMap(String js){
//        return gson.fromJson(js, Map.class);
//    }
    public static <K, V> HashMap<K, V>jsonToMap(String js){
        return gson.fromJson(js, HashMap.class);
    }
    public static <K, V> HashMap<K, V>jsonToMap(Reader js){
        return gson.fromJson(js, HashMap.class);
 
    }
    public static void t(){
        try {
            InputStream is= new URL("www.google.com").openStream();
            InputStreamReader isr = new InputStreamReader(is);
            gson.fromJson(isr, Github.UserRequest.class);
        } catch (MalformedURLException ex) {
            Logger.getLogger(APIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(APIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
