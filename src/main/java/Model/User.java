package Model;

import Github.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class User extends UserRequest implements Serializable{
    private static User mainUser;
    
    public static boolean loadMainUser(){
        UserRequest request = API.getInstance().getMyself();
        if(request == null){
            return false;
        }
        mainUser = new User(request);
        return true;
    }
    
    public static User fromLogin(String login){
        UserRequest request = API.getInstance().getUser(login);
        if(request == null){
            return null;
        }
        return new User(request);
    }
    
    public static User getMainUser(){
        if(mainUser == null){
            loadMainUser();
        }
        return mainUser;
    }
    
    private Rep[] reps;
    private Rep timelineRep;
    private LocalDateTime dateCreated;
    
    private boolean hasMediaRep;
    
    private User(UserRequest request){
        this.copy(request);
        reps = Rep.CreateRepInsts(repos_url);
        
        timelineRep = Rep.CreateRepInst(login, "TimelineMedia");
        hasMediaRep = (timelineRep != null);
        dateCreated = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if(hasMediaRep){
            timelineRep.fillContents();
        }
        
        //timelineRep.getContents().output("");
    }
    /*
    private User(String login){
        UserRequest request = API.getInstance().getUser(login);
        this.copy(request);
        reps = Rep.CreateRepInsts(repos_url);
        
        timelineRep = Rep.CreateRepInst(login, "TimelineMedia");
        hasMediaRep = (timelineRep == null);
        if(hasMediaRep){
            timelineRep.fillContents();
        }
        //timelineRep.getContents().output("");
    }
    */
    public void createTimelineMediaRep(){
        API.getInstance().createRep("TimelineMedia");
    }
    public void fillTimelineMediaRep(){
        for(Rep r : reps){
            API.getInstance().addFile(login, timelineRep.getName(), r.getName()+ "/desc.txt", "Folder init for " + r.getName(), Base64.getEncoder().encodeToString("This was made to store media".getBytes()));
        }
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    
    public Rep getTimelineRep(){
        return timelineRep;
    }

    public Rep[] getReps(){
        return reps;
    }
    public Rep getRepAt(int i){
        return reps[i];
    }
    
    public boolean ownsMediaRep(){
        return hasMediaRep;
    }
    
    public void printReps(){
        System.out.println("Showing " + login + "'s public repositories...");
            int i = 0;
            for(Rep rep : reps){
                System.out.println(i + ". " + rep.toString());
                i++;
            }
    }
}
