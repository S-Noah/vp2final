package Github;

import java.io.Serializable;

public class RepOwner implements Serializable{
    private String login;  
    private int id;
    private String node_id;
   
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
