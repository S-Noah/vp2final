/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import okhttp3.*;

/**
 *
 * @author NoahS
 */
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
}
