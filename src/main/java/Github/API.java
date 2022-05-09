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
        try{
            Request userRequest = APIClient.authedRequest(this.url + "user", "Authorization", "token " + key);
            Response userResponse = APIClient.fire(userRequest);
            if(userResponse.code() == 404){
                return null;
            }
            UserRequest gitUser = gson.fromJson(userResponse.body().string(), UserRequest.class);
            return gitUser;
        }
        catch(IOException e){
            e.printStackTrace();
            this.invalid = true;
        }
        return null;
    }
    /**
     * @param login the user to get.
     * Takes a login and calls the GitHub API endpoint that gets the user with that login.
     * @return A UserRequest instance that captures the responses JSON.
     */
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
    /**
     * @param url a reps URL from a User
     * Takes a URL and calls the GitHub API endpoint that gets a list of repositories.
     * @return an array of RepRequests representing the response's JSON.
     */
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
    /**
     * @param login the owner of the rep.
     * @param repName the name of the rep.
     * Takes a Login and rep name and calls the GitHub API endpoint that gets a single user's repository.
     * @return a RepRequest representing the responses JSON.
     */
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
            
            Request repRequest = APIClient.authedPutRequest(url + "repos/" + login + "/" + rep + "/contents/" + path, body, "Authorization", "token " + key);
            Response repResponse = APIClient.fire(repRequest);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * @param url a languages URL from a rep.
     * @return a HashMap containing names to amounts of bytes that represents the response's JSON.
     */
    public HashMap<String, Double> getLanguages(String url){
        try{
            Request langRequest = APIClient.authedRequest(url, "Authorization", "token " + key);
            Response langResponse = APIClient.fire(langRequest);
            return APIClient.jsonToMap(langResponse.body().string());
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Simple function that gets JSON from a URL.
     * @param url the target URL.
     * @return a list representing the JSON.
     */
    public ArrayList<String> getStringList(String url){
        try{
            Request request = APIClient.authedRequest(url, "Authorization", "token " + key);
            Response response = APIClient.fire(request);
            ArrayList list = gson.fromJson(response.body().string(), ArrayList.class);
            return list;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
       
    }
    /**
     * @param name the name of the new rep.
     * Creates a new rep for the authenticated user.
     */
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
    /**
     * @param login The owner.
     * @param repName The rep name.
     * @param path The path to the target file.
     * Calls the GitHub API to get a file's contents.
     * @return a FileRequest representing the response's JSON.
     */
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
