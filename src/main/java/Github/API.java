/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Github;
import Oauth.OauthTokenRequest;
import com.google.gson.Gson;
import com.mycompany.vp2final.Main;
import data.APIClient;
import java.io.IOException;
import java.io.Serializable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author NoahS
 */
public class API implements Serializable{
    private static Gson gson = new Gson();
    private static String url = "https://api.github.com/";
    
    private static API instance = null;
   
    public static API getInstance(){
        if(instance == null){
            throw new NullPointerException("API Instance must be initiailized... Use the contructor prior to getInstance call.");
        }
        return instance;
    }
    
    private String key;
    private boolean invalid;
    
    public API(String key){
        this.key = key;
        this.invalid = false;
        instance = this;
    }
    
    public UserRequest getMyself(){
        try{
            Request userRequest = APIClient.authedRequest(this.url + "user", "Authorization", "token " + key);
            Response userResponse = APIClient.fire(userRequest);
            UserRequest gitUser = gson.fromJson(userResponse.body().string(), UserRequest.class);
            return gitUser;
        }
        catch(IOException e){
            e.printStackTrace();
            this.invalid = true;
        }
        return null;
    }
    
    public UserRequest getUser(String login){
        try{
            Request userRequest = APIClient.authedRequest(this.url + "users/" + login, "Authorization", "token " + key);
            Response userResponse = APIClient.fire(userRequest);
            UserRequest gitUser = gson.fromJson(userResponse.body().string(), UserRequest.class);
            return gitUser;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }  
    public RepRequest[] getReps(String url){
         try{
            Request repsRequest = APIClient.authedRequest(url, "Authorization", "token " + key);
            Response repsResponse = APIClient.fire(repsRequest);
            RepRequest[] userReps = gson.fromJson(repsResponse.body().string(), RepRequest[].class);
            return userReps;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public RepRequest getRep(String login, String repName){
         try{
            Request request = APIClient.authedRequest(this.url + "repos/" + login + "/" + repName, "Authorization", "token " + key);
            Response response = APIClient.fire(request);
            if(response.code() != 200){
                return null;
            }
            RepRequest repRequest = gson.fromJson(response.body().string(), RepRequest.class);
            return repRequest;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void addFile(String login, String rep, String path, String message, String content){
        try{
            String json = "{"
                    + "\"message\":\""+ message + "\","
                    + "\"committer\":{\"name\":\"" + login + "\", \"email\":\"" + login + "@test.com" + "\"},"
                    + "\"author\":{\"name\":\"" + login + "\", \"email\":\"" + login + "@test.com" + "\"},"
                    + "\"content\":\""+ content + "\""
                    + "}";
            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
            
            Request repRequest = APIClient.authedPutRequest(url + "repos/" + login + "/" + rep + "/contents/" + path, body, "Authorization", "token " + key);
            Response repResponse = APIClient.fire(repRequest);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
  
    public void createRep(String name){
         try{
            String json = "{\"name\":\"" + name + "\"}";
            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
            
            Request repRequest = APIClient.authedPostRequest(url + "user/repos", body, "Authorization", "token " + key);
            Response repResponse = APIClient.fire(repRequest);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public FileRequest[] getContents(String login, String repName, String path){
        Request request = APIClient.authedRequest(url + "repos/" +login + "/" + repName + "/contents/" + path, "Authorization", "token " + key);
        try{
            Response response = APIClient.fire(request);
            return gson.fromJson(response.body().string(), FileRequest[].class);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static String tradeCode(String code){
        String json = 
                "{"
                + "\"client_id\":\"" + Main.client_id + "\","
                + "\"client_secret\":\"" + Main.client_secret + "\","
                + "\"code\":\"" + code + "\""
                + "}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .header("Accept", "application/json")
                .build();
        
        try{
            Response response = APIClient.fire(request);
            OauthTokenRequest tokenRequest = gson.fromJson(response.body().string(), OauthTokenRequest.class);
            return tokenRequest.getAccessToken();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }
    
    public boolean isInvalid(){
        return invalid;
    }
}
