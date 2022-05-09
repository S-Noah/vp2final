package Model;

import Github.API;
import Github.FileRequest;
import Github.RepRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
 
public class Rep extends RepRequest implements Serializable, Comparable<Rep>{
    public static File baseImage = new File("logo.png"); // Image to use when one is not available.
    /**
     * Embedded class that builds from the language GitHubRequest.
     * Store's its total percentage and name.
     * Is used when the languages are drawn on the RepInfoPanel.
     */
    public static class Language implements Comparable<Language>{
        public static StringBuilder sb = new StringBuilder();
        private String name;
        private double numBytes;
        private double percent;
        
        public Language(String name, double numBytes){
            this.name = name;
            this.numBytes = numBytes;
            this.percent = 0.0f;
        }

        public String getName() {
            return name;
        }

        public double getPercent() {
            return percent;
        }

        public void calcPercent(double totalBytes) {
            this.percent = numBytes / totalBytes;
        }
        @Override
        public String toString(){
            sb.setLength(0);
            sb.append(name);
            sb.append(" - ");
            sb.append(String.format("%.1f", percent * 100));
            sb.append("%");
            return sb.toString();
        }

        @Override
        public int compareTo(Language l) {
            return Double.compare(l.percent, this.percent);
        }
    }
    /**
     * @param userRepsUrl URL from the User for reps.
     * Uses GitHub.API to get a users repositories, then it throws out bad candidates and sorts them by Date.
     * @return an array of built up Reps.
     */
    public static Rep[] CreateRepInsts(String userRepsUrl){
        RepRequest requests[] = API.getInstance().getReps(userRepsUrl);
        ArrayList<Rep> validReps = new ArrayList();
        for(RepRequest rr : requests){
            if((!rr.isFork()) && !rr.getName().equals("TimelineMedia")){
                validReps.add(new Rep(rr));
            }
        }
        Collections.sort(validReps);
        return validReps.toArray(new Rep[validReps.size()]);
    }
    /**
     * @param login the owner.
     * @param repName the rep name.
     * Uses the API to get the rep data, and uses it to create a new Rep instance.
     * @return THe new Rep instance.
     */
    public static Rep CreateRepInst(String login, String repName){
        RepRequest rr = API.getInstance().getRep(login, repName);
        if(rr == null){
            return null;
        }
        return new Rep(rr);
    }
   
    /**
     * @param frs list of file requests.
     * Prints frs.
     */
    public static void print(FileRequest frs[]){
        for(FileRequest fr : frs){
            System.out.println(fr.toString());
        }
    }
    
    private Folder contents; // Model.Folder incase the rep need to store it's contents.
    private DefaultMutableTreeNode root; // allows rep to create a tree node out of its contents for Swing purposes.
    private LocalDate dateCreated; // The starting date used for calculation.
    private LocalDate dateLastPushed; // The endind date used for calculation.
    private ArrayList<Language> langs; // The languages that are used in this rep.
    private BufferedImage image; // The Image the user has stored in their Media Rep for this Rep..
    private String desc; // The desc the user has stored in their Media Rep for this Rep.
    private double totalBytes; // The amount of bytes in this Rep.
    
    public Rep(RepRequest repRequest){
        this.copy(repRequest); // Copying data because this class extends RepRequest so they have the same data fields, this is the extended version for the Model.
        contents = new Folder(); 
        langs = new ArrayList<>();
        this.totalBytes = 0;
        dateCreated = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(); // Creates a local date from RepRequest data.
        dateLastPushed = LocalDateTime.parse(pushed_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();// Creates a local date from RepRequest data.
        
        HashMap<String, Double> langMap = API.getInstance().getLanguages(languages_url); // Pulling Hashmap stored in json for languages with their corresponding sizes in bytes.
        for(String key : langMap.keySet()){ // Adding the values up and constructing Languages.
            double bytes = langMap.get(key).doubleValue();
            totalBytes += bytes;
            langs.add(new Language(key, bytes));
        }
        for(Language l : langs){
            l.calcPercent((int)totalBytes); // This has to be done after all Languages have been init.
        }
        Collections.sort(langs);
    }
    /**
     * Prepares Rep contents so the Application can interact with the file structure.
     */
    public void initFolders(){
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
        root = new DefaultMutableTreeNode(name);
        makeTree(contents, root);
    }
    /**
     * Gets file contents and updates the Folder and GFile Structure.
     * Starts off the recursive overloaded version.
     */
    public void fillContents(){
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
    }
    /**
     * @param currentFolder 
     * Overloaded Recursive version that starts from a current folder and created the Folder and File objects for the path until it cannot.
     */
    private void fillContents(Folder currentFolder){
        for(String key : currentFolder.getFolderKeys()){
            Folder f = currentFolder.getSubfolder(key);
            FileRequest frs[] = API.getInstance().getContents(owner.getLogin(), name, f.getPath());
            f.addFileRequests(frs);
            fillContents(f);
        }
    }
    /**
     * @param folder
     * @param root 
     * Copies the folder contents into the DLM root for a Swing Tree.
     */
    public void makeTree(Folder folder, DefaultMutableTreeNode root){
        for(Folder f : folder.getFolderValues()){
            DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(f.getName());
            root.add(fNode);
            makeTree(f, fNode);
        }
        for(GFile f : folder.getFileValues()){
            root.add(new DefaultMutableTreeNode(f.getName()));
        }
    }
    public BufferedImage getImage(){
        if(image != null){
            
        }
        return image;
    }
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateLastPushed() {
        return dateLastPushed;
    }
    
    public Folder getContents(){
        return contents;
    }

    public ArrayList<Language> getLangs() {
        return langs;
    }
    
    public DefaultMutableTreeNode getRoot(){
        return root;
    }
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int compareTo(Rep rep){
        return this.dateCreated.compareTo(rep.getDateCreated()) * -1;
    }
}
