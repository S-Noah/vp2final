package data;

import Model.User;
import java.util.ArrayList;
import java.util.HashMap;

public class UserCache{
    private HashMap<String, User> cache;
    private ArrayList<String> keys;
    private int size;
    private int max;
    
    public UserCache(int max){
        this.max = max;
        cache = new HashMap();
        keys = new ArrayList();
        size = 0;
        
    }
    public void add(User user){
        if(size >= max){
            String lastLogin = keys.remove(0);
            cache.remove(lastLogin);
            size--;
        }
        keys.add(user.getLogin());
        cache.put(user.getLogin(), user);
        size++;
    }
    public User get(String login){
        return cache.get(login);
    }
}
