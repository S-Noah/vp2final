package Oauth;

import com.mycompany.vp2final.Main;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.FileInputStream;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;

public class OauthServer{
    private HttpServer callBackListener;
    
    public OauthServer(){
        try{
            callBackListener = HttpServer.create(new InetSocketAddress("127.0.0.1", 8001), 0);
        }
        catch(IOException e){
            return;
        }
        callBackListener.createContext("/oauth", new CallbackHandler());
        callBackListener.start();
    }
    
    public void stop(){
        callBackListener.stop(5);
    }
     
    private class CallbackHandler implements HttpHandler{
        public void handle(HttpExchange exchange){
           boolean waiting = true;
           String s = exchange.getRequestURI().toString();
           s = s.substring(s.indexOf('=') + 1);
           Main.OauthSuccessEvent(s);
            try{
                FileInputStream fs;
                while(waiting){
                    if(Main.userLoaded){
                        waiting = false;
                    }
                    else if(Main.userLoadFailed){
                        waiting = false;
                    }
                }
                if(Main.userLoaded){
                    fs = new FileInputStream("Oauth/AuthSuccessPage.html");
                }
                else{
                    fs = new FileInputStream("Oauth/AuthFailedPage.html");
                }
                OutputStream os = exchange.getResponseBody();
                
                byte response[] = fs.readAllBytes();
                exchange.sendResponseHeaders(200, response.length);
                os.write(response);
                os.close();
            }
            catch(IOException e){
                
            }
            exchange.close();
        }
    }
}
