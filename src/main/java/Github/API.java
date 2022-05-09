package Github;

import Oauth.OauthTokenRequest;
import com.google.gson.Gson;
import com.mycompany.vp2final.Main;
import data.APIClient;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * This class communicates with the GitHub API using the APIClient Class.
 * It uses the singleton pattern so any class in the project can use it.
 */
public class API implements Serializable{
    private static Gson gson = new Gson();
    private static String url = "https://api.github.com/";
    private static API instance = null;
   
    /**
     * Singleton get method, However it does not create the instance because you would have to pass a token every time.
     *      So the constructor acts as a setter for the singleton instance.
     * @return the singleton instance.
     */
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
    /**
     * Preforms an authenticated request and calls the GitHub API endpoint that gets the authenticated user. 
     * @return A UserRequest instance that captures the responses JSON.
     */
    public UserRequest getMyself(){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(this.url + "user", "Authorization", "token " + key);
            response = APIClient.fire(request);
            if(response.code() != 200){
                response.close();
                return null;
            }
            UserRequest gitUser = gson.fromJson(response.body().string(), UserRequest.class);
            response.close();
            return gitUser;
        }
        catch(IOException e){
            this.invalid = true;
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param login the user to get.
     * Takes a login and calls the GitHub API endpoint that gets the user with that login.
     * @return A UserRequest instance that captures the responses JSON.
     */
    public UserRequest getUser(String login){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(this.url + "users/" + login, "Authorization", "token " + key);
            response = APIClient.fire(request);
            if(response.code() != 200){
                response.close();
                return null;
            }
            UserRequest gitUser = gson.fromJson(response.body().string(), UserRequest.class);
            response.close();
            return gitUser;
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param url a reps URL from a User
     * Takes a URL and calls the GitHub API endpoint that gets a list of repositories.
     * @return an array of RepRequests representing the response's JSON.
     */
    public RepRequest[] getReps(String url){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(url, "Authorization", "token " + key);
            response = APIClient.fire(request);
            if(response.code() != 200){
                response.close();
                return null;
            }
            RepRequest[] userReps = gson.fromJson(response.body().string(), RepRequest[].class);
            response.close();
            return userReps;
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param login the owner of the rep.
     * @param repName the name of the rep.
     * Takes a Login and rep name and calls the GitHub API endpoint that gets a single user's repository.
     * @return a RepRequest representing the responses JSON.
     */
    public RepRequest getRep(String login, String repName){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(this.url + "repos/" + login + "/" + repName, "Authorization", "token " + key);
            response = APIClient.fire(request);
            if(response.code() != 200){
                response.close();
                return null;
            }
            RepRequest repRequest = gson.fromJson(response.body().string(), RepRequest.class);
            response.close();
            return repRequest;
            
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param login the owner.
     * @param rep the name of the rep.
     * @param path the path of files in the rep.
     * @param message the commit message.
     * @param content the Base64 encoded string representing the file.
     * @param sha the file hash REQUIRED FOR UPDATING.
     * Takes all params and adds or updates the specific file at a path in a user's repository.
     */
    public void addFile(String login, String rep, String path, String message, String content, String sha){
        Request request;
        Response response = null;
        try{
            String json = "{"
                    + "\"message\":\""+ message + "\","
                    + "\"committer\":{\"name\":\"" + login + "\", \"email\":\"" + login + "@test.com" + "\"},"
                    + "\"author\":{\"name\":\"" + login + "\", \"email\":\"" + login + "@test.com" + "\"},"
                    + "\"content\":\""+ content + "\","
                    + "\"sha\":\"" + sha + "\""
                    + "}";
            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
            
            request = APIClient.authedPutRequest(url + "repos/" + login + "/" + rep + "/contents/" + path, body, "Authorization", "token " + key);
            response = APIClient.fire(request);
            response.close();
            return;
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
    }
    /**
     * @param url a languages URL from a rep.
     * @return a HashMap containing names to amounts of bytes that represents the response's JSON.
     */
    public HashMap<String, Double> getLanguages(String url){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(url, "Authorization", "token " + key);
            response = APIClient.fire(request);
            String responseBody = response.body().string();
            response.close();
            return APIClient.jsonToMap(responseBody);
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * Simple function that gets JSON from a URL.
     * @param url the target URL.
     * @return a list representing the JSON.
     */
    public ArrayList<String> getStringList(String url){
        Request request;
        Response response = null;
        try{
            request = APIClient.authedRequest(url, "Authorization", "token " + key);
            response = APIClient.fire(request);
            ArrayList list = gson.fromJson(response.body().string(), ArrayList.class);
            response.close();
            return list;
        }
        catch(IOException e){

        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param name the name of the new rep.
     * Creates a new rep for the authenticated user.
     */
    public void createRep(String name){
        Request request;
        Response response = null;
         try{
            String json = "{\"name\":\"" + name + "\"}";
            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
            
            request = APIClient.authedPostRequest(url + "user/repos", body, "Authorization", "token " + key);
            response = APIClient.fire(request);
            response.close();
            return;
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
    }
    /**
     * @param login The owner.
     * @param repName The rep name.
     * @param path The path to the target file.
     * Calls the GitHub API to get a file's contents.
     * @return a FileRequest representing the response's JSON.
     */
    public FileRequest[] getContents(String login, String repName, String path){
        Request request = APIClient.authedRequest(url + "repos/" +login + "/" + repName + "/contents/" + path, "Authorization", "token " + key);
        Response response = null;
        try{
            response = APIClient.fire(request);
            String responseBody = response.body().string();
            response.close();
            return gson.fromJson(responseBody, FileRequest[].class);
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return null;
    }
    /**
     * @param code OAuth handshake code.
     * takes a code from the GitHub OauthHandshake and exchanges it for a Token.
     * @return the token.
     */
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
        Response response = null;
        
        try{
            response = APIClient.fire(request);
            OauthTokenRequest tokenRequest = gson.fromJson(response.body().string(), OauthTokenRequest.class);
            response.close();
            return tokenRequest.getAccessToken();
        }
        catch(IOException e){
            
        }
        if(response != null){
            response.close();
        }
        return "";
    }
    
    public boolean isInvalid(){
        return invalid;
    }
}
