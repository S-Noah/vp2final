/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.vp2final;

import Github.API;
import Oauth.OauthServer;
import Model.Settings;
import Model.User;
import com.formdev.flatlaf.FlatDarculaLaf;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
/**
 *
 * @author NoahS
 */
public class Main{

    /**
     * @param args the command line arguments
     */
    // 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
    private static Settings settings;
    private static MainWin mw;
    public static String client_id = "857a7c08677c3c027965";
    public static String client_secret = "4c848ef04ce448e615f2bb015c82539447b62d99";
    public static OauthServer oauthServer;
    public static boolean userLoaded = false;
    public static boolean settingsLoaded = false;
    
    // https://github.com/login/oauth/authorize?scope=repo&client_id=857a7c08677c3c027965
    // https://github.com/login/oauth/authorize?scope=repo%20delete_repo&client_id=857a7c08677c3c027965
    
    
    public static void signInEvent(String token){
        Settings.getInstance().setToken(token, false);
        new API(token);
        userLoaded = User.loadMainUser();
        if(userLoaded){
            mw.setUser(User.getMainUser());
        }
    }
    
    public static boolean OauthRequestEvent(String code){
        String token = API.tradeCode(code);
        Settings.getInstance().setToken(token, true);
        new API(token);
        userLoaded = User.loadMainUser();
        if(userLoaded){
            mw.setUser(User.getMainUser());
        }
        return userLoaded;
    }
    
    public static void OauthSigninEvent(){
        if(oauthServer == null){
            oauthServer = new OauthServer();
        }
        try{
            Desktop.getDesktop().browse(new URI("https://github.com/login/oauth/authorize?scope=repo%20delete_repo&client_id=857a7c08677c3c027965"));
        }
        catch(URISyntaxException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    } 
    public static void LoginSearchEvent(String login){
        User searchedUser = User.fromLogin(login);
        if(searchedUser != null){
            mw.setUser(searchedUser);
        }
        
    }

    public static void main(String[] args){
        // This approach uses an http request library to directly talk to the github api, More work but more control.
        FlatDarculaLaf.setup();
        mw = new MainWin();
        userLoaded = false;
        settingsLoaded = Settings.load();
        
        if(settingsLoaded){
            if(Settings.getInstance().isOauthToken()){
                System.out.println("Loaded OauthToken");
            }
            new API(Settings.getInstance().getToken());
            userLoaded = User.loadMainUser();
        }
        
        if(userLoaded){
            mw.setUser(User.getMainUser());
        }
        else if(Settings.getInstance().isOauthToken()){
            OauthSigninEvent();
        }
        mw.setVisible(true);
    }
}
