package Model;

import Github.API;
import data.FileIO;
import java.io.Serializable;

public class Settings implements Serializable{
    public static Settings instance;
    public static String filename = "Settings.dat";
    
    public static boolean load(){
        instance = (Settings)FileIO.objectIn(filename);
        if(instance == null){
            getInstance();
            return false;
        }
        else{
            instance.loadedSettings = true;
            return true;
        }
    }
    
    public static Settings getInstance(){
        if(instance == null){
            instance = new Settings();
        }
        return instance;
    }
    
    private String token;
    private boolean loadedSettings;
    private boolean isOauthToken;
    
    private Settings(){
        loadedSettings = false;
    }
    public boolean wasLoaded(){
        return loadedSettings;
    }
    public boolean isOauthToken(){
        return isOauthToken;
    }
    
    public String getToken(){
        return token;
    }
    public void setToken(String token, boolean isOauthToken){
        this.token = token;
        this.isOauthToken = isOauthToken;
        save();
    }
    private void save(){
        FileIO.objectOut(this, filename);
    }
}
