package Github;

import java.io.Serializable;

public class RepOwner implements Serializable{
    private String login;  
    private int id;
    private String node_id;
    // Not Sure if I want to totally keeps these fields yet. 
    /*
    private String avatar_url;
    private String url;
    private String html_url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;
    private String type;
    */

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getNode_id() {
        return node_id;
    }
}
