package Oauth;

public class OauthTokenRequest {
    protected String access_token;
    protected String token_type;
    protected String scope;
    
    public String getAccessToken(){
        return access_token;
    }
}