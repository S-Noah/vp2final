/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Oauth;

/**
 *
 * @author NoahS
 */
public class OauthTokenRequest {
    protected String access_token;
    protected String token_type;
    protected String scope;
    
    public String getAccessToken(){
        return access_token;
    }
}
