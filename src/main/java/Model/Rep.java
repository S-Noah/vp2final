/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Github.API;
import Github.FileRequest;
import Github.RepRequest;
import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;
 
/**
 *
 * @author NoahS
 */
public class Rep extends RepRequest implements Serializable{
    public static Rep[] CreateRepInsts(String userRepsUrl){
        RepRequest requests[] = API.getInstance().getReps(userRepsUrl);
        Rep reps[] = new Rep[requests.length];
        int i = 0;
        for(RepRequest rr : requests){
            reps[i] = new Rep(rr);
            i++;
        }
        return reps;
    }
    
    public static Rep CreateRepInst(String login, String repName){
        RepRequest rr = API.getInstance().getRep(login, repName);
        if(rr == null){
            return null;
        }
        return new Rep(rr);
    }
    
    
    //private FileRequest contents[];
    private Folder contents;
    private DefaultMutableTreeNode root;
    
    public static void print(FileRequest frs[]){
        for(FileRequest fr : frs){
            System.out.println(fr.toString());
        }
    }
    
    public Rep(RepRequest repRequest){
        this.copy(repRequest);
        contents = new Folder();
        /*
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
        contents.addFileRequests(repFileRequests);
        fillContents(contents);
        
        root = new DefaultMutableTreeNode(name);
        makeTree(contents, root);
        */
       
    }
    public Folder getContents(){
        return contents;
    }
    public DefaultMutableTreeNode getRoot(){
        return root;
    }
    public void fillContents(){
        FileRequest repFileRequests[] = API.getInstance().getContents(owner.getLogin(), name, "");
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
        for(File f : folder.getFileValues()){
            root.add(new DefaultMutableTreeNode(f.getName()));
        }
    }
    @Override
    public String toString(){
        return name;
    }
}
