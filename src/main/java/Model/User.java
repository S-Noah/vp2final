package Model;

import Github.*;
import com.mycompany.vp2final.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User extends UserRequest implements Serializable{
    private static User mainUser;
    private static ArrayList<String> followingLogins;
    private static GFile followingFile;
     
    public static ArrayList<String> getFollowingLogins(){
        return followingLogins;
    }
    public static boolean loadMainUser(){
        UserRequest request = API.getInstance().getMyself();
        if(request == null){
            return false;
        }
        mainUser = new User(request, true);
        Main.mainUserLoadedEvent();
        return true;
    }
    
    public static User fromLogin(String login){
        UserRequest request = API.getInstance().getUser(login);
        if(request == null){
            return null;
        }
        return new User(request, false);
    }
    
    public static User getMainUser(){
        if(mainUser == null){
            loadMainUser();
        }
        return mainUser;
    }
    
    private Rep[] reps;
    private Rep timelineRep;
    private LocalDate dateCreated;
    
    private boolean hasMediaRep;
    
    private User(UserRequest request, boolean isMainUser){
        this.copy(request);
        reps = Rep.CreateRepInsts(repos_url);
        
        timelineRep = Rep.CreateRepInst(login, "TimelineMedia");
        hasMediaRep = (timelineRep != null);
        dateCreated = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();
        if(hasMediaRep){
            timelineRep.fillContents();
            if(isMainUser){
                followingFile = timelineRep.getContents().getFile("following.json");
                followingLogins = API.getInstance().getStringList(followingFile.getDownload_url());
            }
        }
        else if(isMainUser){
            createTimelineMediaRep();
            timelineRep = Rep.CreateRepInst(login, "TimelineMedia");
            fillTimelineMediaRep();
        }
    }
    public void unfollow(String login){
        followingLogins.remove(login);
        updateFollowersListFile();
    }
    public void follow(String login){
        followingLogins.add(login);
        updateFollowersListFile();
    }
    public void updateFollowersListFile(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < followingLogins.size(); i++){
            sb.append('\"');
            sb.append(followingLogins.get(i));
            sb.append('\"');
            if(i < followingLogins.size() -1){
                sb.append(',');
            }
        }
        sb.append("]");
        
        API.getInstance().addFile(getMainUser().getLogin(), timelineRep.getName(), "following.json", "Updated Followers file for " + getMainUser().getLogin(), Base64.getEncoder().encodeToString(sb.toString().getBytes()), followingFile.getSha());
        
    }
    public void createTimelineMediaRep(){
        API.getInstance().createRep("TimelineMedia");
    }
    public void fillTimelineMediaRep(){
        try {
            API.getInstance().addFile(login, timelineRep.getName(), "following.json", "Added Followers file for " + login, Base64.getEncoder().encodeToString("[]".getBytes()), "");
            FileInputStream imageStream = new FileInputStream(Rep.baseImage);
            byte[] imageBytes = imageStream.readAllBytes();
            for(Rep r : reps){
                if(r.getName() != "TimelineMedia"){
                    API.getInstance().addFile(login, timelineRep.getName(), r.getName()+ "/" + r.baseImage.getName(), "Folder init for " + r.getName(), Base64.getEncoder().encodeToString(imageBytes), "");
                }
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
           Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LocalDate getDateCreated() {
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
