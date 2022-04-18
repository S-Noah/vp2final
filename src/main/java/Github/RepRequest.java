/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Github;

import java.awt.Graphics;
import java.io.Serializable;

/**
 *
 * @author Noahb
 */
public class RepRequest implements Serializable{
    // Identification Fields.
    protected int id;
    protected String node_id;
    protected String name;
    protected String full_name;
    protected RepOwner owner;
    
    // API Extension URLs.
    protected String html_url;
    protected String commits_url;
    
    // Statistic Fields.
    protected String url;
    protected String language;
    protected String description;
    protected boolean fork;
    protected int forks;
    protected int forks_count;
    protected int stargazers_count;
    protected int watchers_count;
    protected int size;
    protected int subscribers;
    protected String created_at;
    protected String pushed_at;
    
    public String getCreated_at() {
        return created_at;
    }
    public String getPushed_at() {
        return pushed_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getDescription() {
        return description;
    }
    
    public RepOwner getOwner() {
        return owner;
    }

    public String getName(){
        return name;
    }

    public int getId() {
        return id;
    }

    public String getNode_id() {
        return node_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public boolean isFork() {
        return fork;
    }

    public String getCommits_url() {
        return commits_url;
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    public int getForks() {
        return forks;
    }

    public int getForks_count() {
        return forks_count;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public int getSize() {
        return size;
    }

    public int getSubscribers() {
        return subscribers;
    }
    @Override
    public String toString(){
        return name + " : " + id;
    }
    
    public void draw(Graphics g){
        
    }
    
    protected void copy(RepRequest repRequest){
        id = repRequest.getId();
        node_id = repRequest.getNode_id();
        name = repRequest.getName();
        full_name = repRequest.getFull_name();
        owner = repRequest.getOwner();
        html_url = repRequest.getHtml_url();
        description = repRequest.getDescription();
        fork = repRequest.isFork();
        commits_url = repRequest.getCommits_url();
        url = repRequest.getUrl();
        language = repRequest.getLanguage();
        forks = repRequest.getForks();
        forks_count = repRequest.getForks_count();
        stargazers_count = repRequest.getStargazers_count();
        watchers_count = repRequest.getWatchers_count();
        size = repRequest.getSize();
        subscribers = repRequest.getSubscribers();
        created_at = repRequest.getCreated_at();
        pushed_at = repRequest.getPushed_at();
        ;
    }
}
