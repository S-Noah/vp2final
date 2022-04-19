/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Github.FileRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author NoahS
 */
public class Folder extends FileRequest{
    private HashMap<String, Folder> folders;
    private HashMap<String, GFile> files;
    private boolean isRoot;
    private DefaultMutableTreeNode root;
    
    public Folder(){
        super();
        isRoot = true;
        folders = new HashMap();
        files = new HashMap();
    }
    public Folder(FileRequest fr){
        isRoot = false;
        this.copy(fr);
        folders = new HashMap();
        files = new HashMap(); 
    }
    public void addFileRequests(FileRequest frs[]){
        for(FileRequest fr : frs){
            this.add(fr);
        }
    }
    public void add(FileRequest fr){
        //System.out.println(fr.getPath());
        if(fr.getType().equals("dir")){
            folders.put(fr.getName(), new Folder(fr));
        }
        else{
            files.put(fr.getName(), new GFile(fr));
        }
    }
    public void output(String indent){
        for(GFile f : files.values()){
            System.out.println(indent + f.getName());
        }
        for(Folder f : folders.values()){
            System.out.println(indent + f.getName());
            f.output(indent + "   ");
        }
    }
    
    public int getFolderSize(){
        return folders.keySet().size();
    }
    
    public void addSubfolder(String key, FileRequest fr){
        folders.put(key, new Folder(fr));
    }
    public void addFile(String key, FileRequest fr){
        files.put(key, new GFile(fr));
    }
    
    public Folder getSubfolder(String key){
        return folders.get(key);
    }
    public GFile getFile(String key){
        return files.get(key);
    }
    
    public Set<String> getFileKeys(){
        return files.keySet();
    }
    public Collection<GFile> getFileValues(){
        return files.values();
    }
    
    public Set<String> getFolderKeys(){
        return folders.keySet();
    }
    public Collection<Folder> getFolderValues(){
        return folders.values();
    } 
    public FileRequest follow(TreePath path){
        Folder folder = this;
        GFile file;
        for(Object dir : path.getPath()){
            if(dir.toString().contains(".")){
                file = folder.getFile(dir.toString());
                return file;
            }
            folder = folder.getSubfolder(dir.toString());
        }
        return folder;
    }
    
    public void makeTree(Folder folder, DefaultMutableTreeNode node){
        for(Folder f : folder.getFolderValues()){
            DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(f.getName());
            node.add(nextNode);
            makeTree(f, nextNode);
        }
        for(GFile f : folder.getFileValues()){
            node.add(new DefaultMutableTreeNode(f.getName()));
        }
    }
    
    public DefaultMutableTreeNode getTree(){
        if(root == null){
            root = new DefaultMutableTreeNode(name);
            makeTree(this, root);
        }
        return root;
    }
}
