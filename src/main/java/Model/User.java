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
import javax.swing.tree.TreePath;

/**
 * User extends the UserRequest which is just data. This allows it to be more complex version with the same fields.
 * Stores the main user with a singleton concept, also represents other users.
 */
public class User extends UserRequest implements Serializable{
    private static User mainUser; // Singleton main user.
    private static ArrayList<String> followingLogins; // The followed logins of the main user.
    private static GFile followingFile; // The file that stores followed logins in the Media Rep.
     
    public static ArrayList<String> getFollowingLogins(){
        return followingLogins;
    }
    /**
     * Uses the GitHub.API getMyself call to load the authenticated user.
     * Constructs a User Object out of the response and sets up the singleton.
     * Calls callback so the window can respond.
     * @return a true or false indicating if the user was loaded successfully.
     */
    public static boolean loadMainUser(){
        UserRequest request = API.getInstance().getMyself();
        if(request.equals(null)){
            return false;
        }
        else if(request.getLogin() == null){
            return false;
        }
        mainUser = new User(request, true);
        Main.mainUserLoadedEvent();
        return true;
    }
    /**
     * Loads a NON main user from their login using GitHub.API
     * @param login
     * @return the constructed User Object.
     */
    public static User fromLogin(String login){
        UserRequest request = API.getInstance().getUser(login);
        if(request.equals(null)){
            return null;
        }
        else if(request.getLogin() == null){
            return null;
        }
        return new User(request, false);
    }
    /**
     * getter method for the singleton main user.
     * @return 
     */
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
    private boolean isMainUser;
    /**
     * Private User constructor that extends request.
     * @param request the RepRequest response from GitHub.API.
     * @param isMainUser 
     */
    private User(UserRequest request, boolean isMainUser){
        this.copy(request); // Copies request data into this object because they share the same fields.
        this.isMainUser = isMainUser;
        
        reps = Rep.CreateRepInsts(repos_url); // Constructs this users reps.
        dateCreated = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(); // Parses the date of account creation used for calculation.
        
        timelineRep = Rep.CreateRepInst(login, "TimelineMedia"); // Tries to load a Media rep if the user has one.
        this.hasMediaRep = (timelineRep != null); // Sets a bool indicating if they do.
        
        if(hasMediaRep){ // If they do.
            timelineRep.fillContents(); // Fill the contents so the application can present them.
            if(isMainUser){
                followingFile = timelineRep.getContents().getFile("following.json"); // Set the following file to the timeline contents.
                followingLogins = API.getInstance().getStringList(followingFile.getDownload_url()); //get the list of logins from the download URL.
            }
        }
        else if(isMainUser){ // If the Media Rep was not found, but is the main user.
            createTimelineMediaRep();
            timelineRep = Rep.CreateRepInst(login, "TimelineMedia"); // Retry timeline call after creation.
            fillTimelineMediaRep(); // Init the timeline rep's contents.
            timelineRep.fillContents();// Fill the Model.Folder's file contents iwth the timeline rep.
            hasMediaRep = true;
            followingFile = timelineRep.getContents().getFile("following.json"); // Retry setting the following file.
            followingLogins = API.getInstance().getStringList(followingFile.getDownload_url()); // Retry extracting logins.
        }
    }
    /**
     * @param login the login to unfollow.
     * Removes the login from the local list, and issues a request to remove it from the timeline media rep.
     */
    public void unfollow(String login){
        followingLogins.remove(login);
        updateFollowersListFile();
    }
    /**
     * @param login the login to follow.
     * Adds the login to the local list, and issues a request to add it to the timeline media rep.
     */
    public void follow(String login){
        followingLogins.add(login);
        System.out.println(followingLogins);
        updateFollowersListFile();
    }
    /**
     * Construct the JSON for the list of followed logins and update the Media Rep.
     */
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
    /**
     * This method stores data for the user on GitHub for GitSocial.
     * @param repName The rep name to update.
     * @param imageData The new image data.
     * @param descData The new desc data.
     * Gets the file SHA for this reps contents and updates the files with the new data.
     */
    public void updateRepMedia(String repName, byte[] imageData, byte[] descData){
        TreePath imagePath = new TreePath(new String[]{repName, "logo.png"});
        TreePath descPath = new TreePath(new String[]{repName, "desc.txt"});
        FileRequest imageFile = timelineRep.getContents().follow(imagePath);
        FileRequest descFile = timelineRep.getContents().follow(descPath);
        
        API.getInstance().addFile(getMainUser().getLogin(), timelineRep.getName(), repName + "/logo.png", "Updated logo file for " + getMainUser().getLogin(), Base64.getEncoder().encodeToString(imageData), imageFile.getSha());
        API.getInstance().addFile(getMainUser().getLogin(), timelineRep.getName(), repName + "/desc.txt", "Updated desc file for " + getMainUser().getLogin(), Base64.getEncoder().encodeToString(descData), descFile.getSha());
    }
    /**
     * Uses GitHub.API to add a rep to the user's account.
     */
    public void createTimelineMediaRep(){
        API.getInstance().createRep("TimelineMedia");
    }
    /**
     * Initializes the Media Rep for the user. Takes every valid Rep and creates a Dir for it, which stores an image and desc.
     * It also creates an empty list of followed logins.
     */
    public void fillTimelineMediaRep(){
        try {
            FileInputStream imageStream = new FileInputStream(Rep.baseImage);
            byte[] imageBytes = imageStream.readAllBytes();
            String desc;
            
            API.getInstance().addFile(login, timelineRep.getName(), "following.json", "Added Followers file for " + login, Base64.getEncoder().encodeToString("[]".getBytes()), "");
            for(Rep r : reps){
                if(r.getName() != "TimelineMedia"){
                    desc = "This is the description for " + r.getName() + "...";
                    API.getInstance().addFile(login, timelineRep.getName(), r.getName()+ "/" + r.baseImage.getName(), "Image init for " + r.getName(), Base64.getEncoder().encodeToString(imageBytes), "");
                    API.getInstance().addFile(login, timelineRep.getName(), r.getName()+ "/" + "desc.txt", "Description init for " + r.getName(), Base64.getEncoder().encodeToString(desc.getBytes()), "");
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
    
    public boolean isMainUser(){
        return isMainUser;
    }
    /**
     * Debug / Depreciated.
     */
    public void printReps(){
        System.out.println("Showing " + login + "'s public repositories...");
            int i = 0;
            for(Rep rep : reps){
                System.out.println(i + ". " + rep.toString());
                i++;
            }
    }
}
