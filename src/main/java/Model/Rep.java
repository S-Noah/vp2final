package Model;

import Github.API;
import Github.FileRequest;
import Github.RepRequest;
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
    public static File baseImage = new File("logo.png");
    public static class Language{
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
    }
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
    
    public static Rep CreateRepInst(String login, String repName){
        RepRequest rr = API.getInstance().getRep(login, repName);
        if(rr == null){
            return null;
        }
        return new Rep(rr);
    }
   
    
    public static void print(FileRequest frs[]){
        for(FileRequest fr : frs){
            System.out.println(fr.toString());
        }
    }
    
    //private FileRequest contents[];
    private Folder contents;
    private DefaultMutableTreeNode root;
    private LocalDate dateCreated;
    private LocalDate dateLastPushed;
    private ArrayList<Language> langs;
    private double totalBytes;
    
    public Rep(RepRequest repRequest){
        this.copy(repRequest);
        contents = new Folder();
        langs = new ArrayList<>();
        this.totalBytes = 0;
        dateCreated = LocalDateTime.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();
        dateLastPushed = LocalDateTime.parse(pushed_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();
        
        HashMap<String, Double> langMap = API.getInstance().getLanguages(languages_url);
        for(String key : langMap.keySet()){
            double bytes = langMap.get(key).doubleValue();
            totalBytes += bytes;
            //System.out.println((int)langMap.get(key));
            langs.add(new Language(key, bytes));
        }
        for(Language l : langs){
            l.calcPercent((int)totalBytes);
        }
        /*
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
        
        root = new DefaultMutableTreeNode(name);
        makeTree(contents, root);
        */
    }
    
    public void initFolders(){
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
        
        root = new DefaultMutableTreeNode(name);
        makeTree(contents, root);
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
    public void fillContents(){
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
//        for(FileRequest fr : repFileRequests){
//            System.out.println(fr);
//        }
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
        
    }
    private void fillContents(Folder currentFolder){
        for(String key : currentFolder.getFolderKeys()){
            Folder f = currentFolder.getSubfolder(key);
            FileRequest frs[] = API.getInstance().getContents(owner.getLogin(), name, f.getPath());
            f.addFileRequests(frs);
            fillContents(f);
        }
    }
    
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
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int compareTo(Rep rep){
        return this.dateCreated.compareTo(rep.getDateCreated()) * -1;
    }
}
