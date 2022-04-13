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
            new API(instance.token);
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
    boolean loadedSettings;
    
    private Settings(){
        loadedSettings = false;
    }
    public boolean wasLoaded(){
        return loadedSettings;
    }
    
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
        save();
    }
    private void save(){
        FileIO.objectOut(this, filename);
    }
}
