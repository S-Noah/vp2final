package com.mycompany.vp2final;

import Github.API;
import Oauth.OauthServer;
import Model.Settings;
import Model.User;
import com.formdev.flatlaf.FlatDarculaLaf;
import data.APIClient;
import data.UserCache;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
public class Main{

    private static Settings settings;
    public static MainWin mw; // Some ckasses need to talk to the main window, just as they do main.
    private static UserCache userCache; // Cache for User searches, optimized feature.
    public static OauthServer oauthServer; // Server incase user needs to sign in.
    public static HashMap<String, String> langColors; // Hashmap of GitHub's languages to colors.
    
    public static String client_id = "857a7c08677c3c027965"; // GitHub App Data.
    public static String client_secret = "4c848ef04ce448e615f2bb015c82539447b62d99"; // GitHub App Data.
   
    public static boolean userLoaded = false; // Flag for incase a user load fails.
    public static boolean settingsLoaded = false;// Flag for incase a Setting load fails.
    public static boolean userLoadFailed = false;
    
    /**
     * When this event method is called the OAuth server is started, and the user is directed to GitHub Login.
     * The server will call OauthSuccessEvent when if it is successful.
     */
    public static void OauthSigninEvent(){
        if(oauthServer == null){
            oauthServer = new OauthServer();
        }
        try{
            Desktop.getDesktop().browse(new URI("https://github.com/login/oauth/authorize?scope=repo%20delete_repo&client_id=857a7c08677c3c027965"));
        }
        catch(URISyntaxException e){
            
        }
        catch(IOException e){
            
        }
    } 
    /**
     * When this event method is called, it trades the code from the login for a token, it then tries to re load the main user.
     * @param code 
     */
    public static void OauthSuccessEvent(String code){
        String token = API.tradeCode(code);
        Settings.getInstance().setToken(token, true); // Saving settings based off of token.
        new API(token); // Setting API to use new token.
        Thread userLoader = new Thread(() -> User.loadMainUser()); // Starting user load thread which will call mainUserLoadedEvent to update the view.
        userLoader.start();
        mw.startLoading(userLoader); // Makes the application load until the thread is done.
    }
    /**
     * This function is for Personal Access Tokens. Since we already have the token, we retry loading the main user.
     * @param token 
     */
    public static void tokenSignInEvent(String token){
        Settings.getInstance().setToken(token, false);
        new API(token);
        Thread userLoader = new Thread(() -> User.loadMainUser()); 
        userLoader.start();
        mw.startLoading(userLoader);
    }
    /**
     * When the main user is successfully loaded, the Application is alerted.
     */
    public static void mainUserLoadedEvent(boolean loaded){
        userLoaded = loaded;
        if(userLoaded)
            mw.setUser(User.getMainUser());
        else{
            userLoadFailed = true;
            mw.displayError("Error Loading User", 10000);
        }
    }
    /**
     * When another user is searched for, check the cache for an existing search, if not use GitHub.API to search. Update cache and Application.
     * @param login 
     */
    public static void LoginSearchEvent(String login){
        User searchedUser = userCache.get(login);
        if(searchedUser == null){
            searchedUser = User.fromLogin(login);
            if(searchedUser == null){
                mw.displayError("Login does not exist.", 3000);
            }
            else{
                userCache.add(searchedUser);
                mw.setUser(searchedUser);
            }
        }
        else{
            mw.setUser(searchedUser);
        }
    }
    /**
     * Function to load the file into the HashMap for the Language Colors.
     */
    public static void loadGithubColors(){
        try{
            Reader jsonFile = new FileReader("githubLangColors.json");
            langColors = APIClient.jsonToMap(jsonFile);
        }
        catch(FileNotFoundException e){
            
        }
    }

    public static void main(String[] args){
        // Setup
        FlatDarculaLaf.setup(); // Start Laf Manager.
        loadGithubColors(); // Load the Language Colors.
        settingsLoaded = Settings.load(); // Try to read local settings.
        // Init
        mw = new MainWin();
        userCache = new UserCache(3);
        userLoaded = false;
        
        if(settingsLoaded){ // If the settings were successfully loaded, try use the loaded token and load the main user.
            new API(Settings.getInstance().getToken());
            Thread userLoader = new Thread(() -> User.loadMainUser()); 
            userLoader.start();
            mw.startLoading(userLoader);
        }
        // Else wait for the user to login.
        mw.setVisible(true);
    }
}
